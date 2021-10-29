package com.changgou.service;

import com.changgou.goods.pojo.Spec;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SpecService {
    /***
     * Spec多条件分页查询
     * @param spec
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spec> findpage(Spec spec, int page, int size);

    /***
     * Spec分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spec> findpage(int page, int size);

    /***
     * Spec多条件搜索方法
     * @param spec
     * @return
     */
    List<Spec> findList(Spec spec);

    /***
     * 删除Spec
     * @param id
     */


    void delete(int id);

    /***
     * 修改Spec数据
     * @param spec
     */
    void update(Spec spec);

    /***
     * 新增Spec
     * @param spec
     */
    void add(Spec spec);

    /**
     * 根据ID查询Spec
     *
     * @param id
     * @return
     */
    Spec findById(Integer id);

    /***
     * 查询所有Spec
     * @return
     */
    List<Spec> findAll();

    List<Spec> findBycategoryid(Integer categoryid);
}
