package com.changgou.service.impl;

import com.changgou.dao.CategoryMapper;
import com.changgou.dao.TemplateMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Template;
import com.changgou.service.TemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    private Example example(Template template) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if (template.getName() != null && !"".equals(template.getName())) {
            criteria.andLike("name", "%" + template.getName() + "%");
        }
        if (template.getId() != null && !"".equals(template.getId())) {
            criteria.andEqualTo("id", template.getId());
        }
        if (template.getSpecNum() != null && !"".equals(template.getSpecNum())) {
            criteria.andEqualTo("specnum", template.getSpecNum());
        }
        if (template.getParaNum() != null && !"".equals(template.getParaNum())) {
            criteria.andEqualTo("paranum", template.getParaNum());
        }
        return example;
    }

    @Override
    public PageInfo<Template> findpage(Template template, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = example(template);
        List<Template> templates = templateMapper.selectByExample(example);
        return new PageInfo<>(templates);
    }

    @Override
    public PageInfo<Template> findpage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Template> templates = templateMapper.selectAll();
        return new PageInfo<>(templates);
    }

    @Override
    public List<Template> findList(Template template) {
        Example example = example(template);
        return templateMapper.selectByExample(example);
    }

    @Override
    public void delete(int id) {
        templateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Template template) {
        templateMapper.updateByPrimaryKeySelective(template);
    }

    @Override
    public void add(Template template) {
        templateMapper.insert(template);
    }

    @Override
    public Template findById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Template> findAll() {
        return templateMapper.selectAll();
    }

    @Override
    public List<Template> findByCategory(Integer id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        Template template = new Template();
        template.setId(category.getTemplateId());
        return templateMapper.select(template);
    }

}
