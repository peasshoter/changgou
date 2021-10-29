package com.changgou.service.impl;

import com.changgou.dao.CategoryMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    private Example example(Category category) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if (category.getName() != null && !"".equals(category.getName())) {
            criteria.andLike("name", "%" + category.getName() + "%");
        }
        if (category.getId() != null && !"".equals(category.getId())) {
            criteria.andEqualTo("id", category.getId());
        }
        if (category.getGoodsNum() != null && !"".equals(category.getGoodsNum())) {
            criteria.andEqualTo("goodsnum", category.getGoodsNum());
        }
        if (category.getIsMenu() != null && !"".equals(category.getIsMenu())) {
            criteria.andEqualTo("ismenu", category.getIsMenu());
        }
        if (category.getIsShow() != null && !"".equals(category.getIsShow())) {
            criteria.andEqualTo("isshow", category.getIsShow());
        }
        if (category.getSeq() != null && !"".equals(category.getSeq())) {
            criteria.andEqualTo("seq", category.getSeq());
        }
        if (category.getParentId() != null && !"".equals(category.getParentId())) {
            criteria.andEqualTo("parentid", category.getParentId());
        }
        if (category.getTemplateId() != null && !"".equals(category.getTemplateId())) {
            criteria.andEqualTo("templateid", category.getTemplateId());
        }
        return example;
    }

    @Override
    public PageInfo<Category> findpage(Category category, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = example(category);
        List<Category> categorys = categoryMapper.selectByExample(example);
        return new PageInfo<>(categorys);
    }

    @Override
    public PageInfo<Category> findpage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Category> categorys = categoryMapper.selectAll();
        return new PageInfo<>(categorys);
    }

    @Override
    public List<Category> findList(Category category) {
        Example example = example(category);
        return categoryMapper.selectByExample(example);
    }

    @Override
    public void delete(int id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public void add(Category category) {
        categoryMapper.insert(category);
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Category> findByParentId(Integer parentid) {
        Category category = new Category();
        category.setParentId(parentid);
        return categoryMapper.select(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }
}
