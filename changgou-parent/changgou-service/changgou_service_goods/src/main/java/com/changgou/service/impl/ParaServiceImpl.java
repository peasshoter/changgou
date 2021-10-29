package com.changgou.service.impl;

import com.changgou.dao.CategoryMapper;
import com.changgou.dao.ParaMapper;
import com.changgou.dao.TemplateMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.pojo.Template;
import com.changgou.service.ParaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ParaServiceImpl implements ParaService {
    @Autowired
    private ParaMapper paraMapper;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    private Example example(Para para) {
        Example example = new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        if (para.getName() != null && !"".equals(para.getName())) {
            criteria.andLike("name", "%" + para.getName() + "%");
        }
        if (para.getId() != null && !"".equals(para.getId())) {
            criteria.andEqualTo("id", para.getId());
        }
        if (para.getOptions() != null && !"".equals(para.getOptions())) {
            criteria.andEqualTo("options", para.getOptions());
        }
        if (para.getSeq() != null && !"".equals(para.getSeq())) {
            criteria.andEqualTo("seq", para.getSeq());
        }
        if (para.getTemplateId() != null && !"".equals(para.getTemplateId())) {
            criteria.andEqualTo("templateid", para.getTemplateId());
        }
        return example;
    }

    @Override
    public PageInfo<Para> findpage(Para para, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = example(para);
        List<Para> paras = paraMapper.selectByExample(example);
        return new PageInfo<>(paras);
    }

    @Override
    public PageInfo<Para> findpage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Para> paras = paraMapper.selectAll();
        return new PageInfo<>(paras);
    }

    @Override
    public List<Para> findList(Para para) {
        Example example = example(para);
        return paraMapper.selectByExample(example);
    }

    @Override
    public void delete(int id) {
        paraMapper.deleteByPrimaryKey(id);
        Para para = paraMapper.selectByPrimaryKey(id);
        updateParaNum(para, -1);
    }

    @Override
    public void update(Para para) {
        paraMapper.updateByPrimaryKeySelective(para);
    }

    @Override
    public void add(Para para) {
        paraMapper.insert(para);
        updateParaNum(para, -1);
    }

    @Override
    public Para findById(Integer id) {
        return paraMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Para> findAll() {
        return paraMapper.selectAll();
    }

    @Override
    public List<Para> findByCategory(Integer id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        Para para = new Para();
        para.setTemplateId(category.getTemplateId());
        return paraMapper.select(para);
    }

    public void updateParaNum(Para para, int count) {
        Template template = templateMapper.selectByPrimaryKey(para.getTemplateId());
        template.setSpecNum(template.getSpecNum() + count);
        templateMapper.updateByPrimaryKeySelective(template);
    }
}
