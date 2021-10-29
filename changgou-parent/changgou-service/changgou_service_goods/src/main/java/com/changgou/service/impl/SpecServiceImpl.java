package com.changgou.service.impl;

import com.changgou.dao.CategoryMapper;
import com.changgou.dao.SpecMapper;
import com.changgou.dao.TemplateMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.pojo.Template;
import com.changgou.service.SpecService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecServiceImpl implements SpecService {
    @Autowired
    private SpecMapper specMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    private Example example(Spec spec) {
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if (spec.getName() != null && !"".equals(spec.getName())) {
            criteria.andLike("name", "%" + spec.getName() + "%");
        }
        if (spec.getId() != null && !"".equals(spec.getId())) {
            criteria.andEqualTo("id", spec.getId());
        }
        if (spec.getOptions() != null && !"".equals(spec.getOptions())) {
            criteria.andEqualTo("options", spec.getOptions());
        }
        if (spec.getSeq() != null && !"".equals(spec.getSeq())) {
            criteria.andEqualTo("seq", spec.getSeq());
        }
        if (spec.getTemplateId() != null && !"".equals(spec.getTemplateId())) {
            criteria.andEqualTo("templateid", spec.getTemplateId());
        }
        return example;
    }

    @Override
    public PageInfo<Spec> findpage(Spec spec, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = example(spec);
        List<Spec> specs = specMapper.selectByExample(example);
        return new PageInfo<>(specs);
    }

    @Override
    public PageInfo<Spec> findpage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Spec> specs = specMapper.selectAll();
        return new PageInfo<>(specs);
    }

    @Override
    public List<Spec> findList(Spec spec) {
        Example example = example(spec);
        return specMapper.selectByExample(example);
    }

    @Override
    public void delete(int id) {
        specMapper.deleteByPrimaryKey(id);
        Spec spec = specMapper.selectByPrimaryKey(id);
        updateSpecNum(spec, -1);
    }

    @Override
    public void update(Spec spec) {
        specMapper.updateByPrimaryKeySelective(spec);
    }

    @Override
    public void add(Spec spec) {
        specMapper.insert(spec);

        updateSpecNum(spec, 1);
    }

    @Override
    public Spec findById(Integer id) {
        return specMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }

    @Override
    public List<Spec> findBycategoryid(Integer categoryid) {
        Category category = categoryMapper.selectByPrimaryKey(categoryid);
        Spec spec = new Spec();
        spec.setTemplateId(category.getTemplateId());
        return specMapper.select(spec);
    }

    public void updateSpecNum(Spec spec, int count) {
        Template template = templateMapper.selectByPrimaryKey(spec.getTemplateId());
        template.setSpecNum(template.getSpecNum() + count);
        templateMapper.updateByPrimaryKeySelective(template);
    }
}
