package com.changgou.search.service.Impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import entity.Result;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SkuEsMapper esMapper;
    @Autowired
    private ElasticsearchTemplate estemplate;

    @Override
    public void importSku() {
        Result<List<Sku>> skuFeignAll = skuFeign.findAll();
        //skuinfo->string->list
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(skuFeignAll.getData()), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            //list->map
            Map<String, Object> jsonObject = JSON.parseObject(skuInfo.getSpec());
            skuInfo.setSpecMap(jsonObject);

        }
        esMapper.saveAll(skuInfos);
    }

    @Override
    public Map search(Map<String, String> searchMap) {
        String keywords = searchMap.get("keywords");

        //2.判断是否为空 如果 为空 给一个默认 值:华为
        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";
        }
        //3.创建 查询构建对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //4.设置 查询的条件

        // 4.1 商品分类的列表展示: 按照商品分类的名称来分组
        //terms  指定分组的一个别名
        //field 指定要分组的字段名
        //size 指定查询结果的数量 默认是10个
        queryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("categoryName").size(50));

        //4.2 商品的品牌的列表展示  按照商品品牌来进行分组
        queryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgroup").field("brandName").size(100));

        //4.3 商品的规格的列表展示   按照商品的规格的字段spec 进行分组
        //规则 要求 字段 是一个keyword类型的  spec.keyword
        queryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(500));


        //匹配查询  先分词 再查询  主条件查询
        //参数1 指定要搜索的字段
        //参数2 要搜索的值(先分词 再搜索)
        //nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", keywords));
        //从多个字段中搜索数据
        queryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords, "name", "categoryName", "brandName"));


        //========================过滤查询 开始=====================================

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //4.4 过滤查询的条件设置   商品分类的条件
        String category = searchMap.get("category");

        if (!StringUtils.isEmpty(category)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName", category));
        }
        //4.5 过滤查询的条件设置   商品品牌的条件
        String brand = searchMap.get("brand");

        if (!StringUtils.isEmpty(brand)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName", brand));
        }
        //排序
        String sortFiled = searchMap.get("sortfiled");
        String sortType = searchMap.get("sorttype");
        if (!StringUtils.isEmpty(sortFiled) && !StringUtils.isEmpty(sortType)) {
            queryBuilder.withSort(new FieldSortBuilder(sortFiled).order(sortType.equals("DESC") ? SortOrder.DESC : SortOrder.ASC));
        }
//规格筛选
        if (searchMap != null) {
            for (String key : searchMap.keySet()) {
                if (key.startsWith("spec_")) {
                    boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", searchMap.get(key)));
                }
            }
        }

        Integer pageNum = 1;
        if (!StringUtils.isEmpty(searchMap.get("pageNum"))) {
            pageNum = Integer.valueOf(searchMap.get("pageNum"));

        }
        Integer pageSize = 10;
        queryBuilder.withPageable(PageRequest.of(pageNum - 1, pageSize));
//        //4.4 过滤查询的条件设置   商品分类的条件

        queryBuilder.withFilter(boolQueryBuilder);

        HashMap<String, Object> map = searchList(queryBuilder);
        List<String> brandList = getStringsBrandList(queryBuilder);
        map.put("brandName", brandList);
        List<String> categoryList = getStringsCategoryList(queryBuilder);
        map.put("categoryName", categoryList);
        Map<String, Set<String>> specList = getStringsSpecList(queryBuilder);
        map.put("specList", specList);
        map.put("pagenum", pageNum);
        map.put("pagesize", 30);

        return map;
    }

    //搜索结果
    private HashMap<String, Object> searchList(NativeSearchQueryBuilder queryBuilder) {
        HighlightBuilder.Field field = new HighlightBuilder.Field("");

        queryBuilder.withHighlightFields();

        AggregatedPage<SkuInfo> skuInfos = estemplate.queryForPage(queryBuilder.build(), SkuInfo.class);

        long totalElements = skuInfos.getTotalElements();
        int totalPages = skuInfos.getTotalPages();

        List<SkuInfo> content = skuInfos.getContent();

        HashMap<String, Object> map = new HashMap<>();
        map.put("rows", content);
        map.put("total", totalElements);
        map.put("totalPages", totalPages);
        return map;
    }

    /**
     * 获取分类数据
     *
     * @param queryBuilder
     * @return
     */
    private List<String> getStringsCategoryList(NativeSearchQueryBuilder queryBuilder) {
        //添加聚合操作
        queryBuilder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        AggregatedPage<SkuInfo> aggregatedPages = estemplate.queryForPage(queryBuilder.build(), SkuInfo.class);

        //获取分组数据
        StringTerms skuCategory = aggregatedPages.getAggregations().get("skuCategory");
        List<String> categoryList = new ArrayList<>();
        for (StringTerms.Bucket bucket : skuCategory.getBuckets()) {
            String category = bucket.getKeyAsString();
            categoryList.add(category);
        }
        return categoryList;
    }

    /**
     * 获取品牌数据
     *
     * @param queryBuilder
     * @return
     */
    private List<String> getStringsBrandList(NativeSearchQueryBuilder queryBuilder) {
        //添加聚合操作
        queryBuilder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        AggregatedPage<SkuInfo> aggregatedPages = estemplate.queryForPage(queryBuilder.build(), SkuInfo.class);

        //获取分组数据
        StringTerms skuCategory = aggregatedPages.getAggregations().get("skuBrand");
        List<String> brandList = new ArrayList<>();
        for (StringTerms.Bucket bucket : skuCategory.getBuckets()) {
            String category = bucket.getKeyAsString();
            brandList.add(category);
        }
        return brandList;
    }

    /**
     * 获取规格数据
     *
     * @param queryBuilder
     * @return
     */
    private Map<String, Set<String>> getStringsSpecList(NativeSearchQueryBuilder queryBuilder) {
        //添加聚合操作
        queryBuilder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword"));
        AggregatedPage<SkuInfo> aggregatedPages = estemplate.queryForPage(queryBuilder.build(), SkuInfo.class);

        //获取分组数据
        StringTerms skuSpec = aggregatedPages.getAggregations().get("skuSpec");
        List<String> specList = new ArrayList<>();
        for (StringTerms.Bucket bucket : skuSpec.getBuckets()) {
            String category = bucket.getKeyAsString();
            specList.add(category);
        }
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Set<String> values = new HashSet<>();
        for (String spec : specList) {
            Map<String, String> object = JSON.parseObject(spec, Map.class);
            for (Map.Entry<String, String> entry : object.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                values = map.get(key);

                if (values == null) {
                    values = new HashSet<String>();
                }
                values.add(value);
                map.put(key, values);
            }
        }
        return map;
    }
}
