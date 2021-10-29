package com.changgou.service.impl;

import com.changgou.dao.BrandMapper;
import com.changgou.dao.CategoryBrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    public Example createExample(Brand brand) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (brand != null) {
            if (!"".equals(brand.getName()) && brand.getName() != null) {
                criteria.andLike("name", "%" + brand.getName() + "%");
            }
            if (!"".equals(brand.getLetter()) && brand.getLetter() != null) {
                criteria.andEqualTo("letter", brand.getLetter());
            }
        }
        return example;
    }


    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(Integer id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        return brand;
    }

    @Override
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(int id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Brand> findList(Brand brand) {
        Example example1 = createExample(brand);

        return brandMapper.selectByExample(example1);
    }

    @Override
    public PageInfo<Brand> findPage(Integer page, Integer Size) {
        PageHelper.startPage(page, Size);
        List<Brand> brands = brandMapper.selectAll();

        return new PageInfo<Brand>(brands);
    }

    @Override
    public PageInfo<Brand> findPage(Brand brand, Integer page, Integer Size) {
        PageHelper.startPage(page, Size);
        Example example = createExample(brand);

        List<Brand> brands = brandMapper.selectByExample(example);

        return new PageInfo<Brand>(brands);
    }

    @Override
    public List<Brand> findByCategory(Integer categoryid) {
        return brandMapper.findByCatogory(categoryid);
    }


}
