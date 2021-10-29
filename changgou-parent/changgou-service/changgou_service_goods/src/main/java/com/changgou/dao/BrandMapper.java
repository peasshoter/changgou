package com.changgou.dao;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface BrandMapper extends Mapper<Brand> {
    @Select("SELECT tb_brand.* from tb_brand,tb_category_brand where tb_brand.id=tb_category_brand.brand_id and tb_category_brand.category_id={categoryid}")
    List<Brand> findByCatogory(Integer categoryid);
}
