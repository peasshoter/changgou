package com.changgou.item.service.Impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.item.service.PageService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private CategoryFeign categoryFeign;
    @Autowired
    private SpuFeign spuFeign;


    @Autowired
    private TemplateEngine templateEngine;

    @Value("${pagepath}")
    private String pagepath;

    private Map<String, Object> buildDataModel(Long spuid) {
        //构建数据模型
        HashMap<String, Object> dataMap = new HashMap<>();
        //获取spu 和SKU列表
        Result<Spu> spuById = spuFeign.findById(spuid);
        Spu spu = spuById.getData();
        dataMap.put("spu", spu);
        //根据spuId查询Sku集合
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        Result<List<Sku>> skulist = skuFeign.findList(sku);
        dataMap.put("skuList", skulist);
        //获取分类信息
        //面包导航
        Category category1 = categoryFeign.findById(spu.getCategory1Id()).getData();
        Category category2 = categoryFeign.findById(spu.getCategory2Id()).getData();
        Category category3 = categoryFeign.findById(spu.getCategory3Id()).getData();
        dataMap.put("category1", category1);
        dataMap.put("category2", category2);
        dataMap.put("category3", category3);
        //图片
        if (spu.getImages() != null) {
            dataMap.put("imageList", spu.getImages().split(","));
        }
        //规格map转json
        dataMap.put("specificationList", JSON.parseObject(spu.getSpecItems(), Map.class));

        return dataMap;
    }


    @Override
    public void createPageHtml(Long spuId) {
        Context context = new Context();
        Map<String, Object> dataModel = buildDataModel(spuId);
        context.setVariables(dataModel);

        File path = new File(pagepath);
        if (!path.exists()) {
            path.mkdir();
        }

        File dest = new File(path, spuId + ".html");
        try {
            PrintWriter printWriter = new PrintWriter(dest, "Utf8");
            templateEngine.process("item", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
