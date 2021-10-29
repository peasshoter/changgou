package com.changgou.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BrandService {

    List<Brand> findAll();

    Brand findById(Integer id);

    void add(Brand brand);

    void update(Brand brand);

    void delete(int id);

    List<Brand> findList(Brand brand);

    PageInfo<Brand> findPage(Integer page, Integer Size);

    PageInfo<Brand> findPage(Brand brand, Integer page, Integer Size);

    List<Brand> findByCategory(Integer categoryid);
}
