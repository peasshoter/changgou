package com.changgou.controller;

import com.changgou.goods.pojo.Spec;
import com.changgou.service.SpecService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/spec")
public class SpecController {
    @Autowired
    private SpecService specService;

    /***
     * 查询Spec全部数据
     * @return
     */
    @GetMapping
    public Result<Spec> findAll() {
        List<Spec> all = specService.findAll();
        return new Result<Spec>(true, StatusCode.OK, "查询所有成功", all);
    }

    /***
     * 根据ID查询Spec数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spec> findById(@PathVariable Integer id) {
        Spec byId = specService.findById(id);
        return new Result<Spec>(true, StatusCode.OK, "查询id成功", byId);
    }

    /***
     * 新增Spec数据
     * @param spec
     * @return
     */
    @PostMapping
    public Result<Spec> add(@RequestBody Spec spec) {
        specService.add(spec);
        return new Result<Spec>(true, StatusCode.OK, "添加成功");
    }

    /***
     * 修改Spec数据
     * @param spec
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Spec> update(@PathVariable int id, @RequestBody Spec spec) {
        spec.setId(id);
        specService.update(spec);
        return new Result<Spec>(true, StatusCode.OK, "更新成功");
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Spec> del(@PathVariable int id) {
        specService.delete(id);
        return new Result<Spec>(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param spec
     * @return
     */
    @PostMapping("/search")
    public Result<Spec> findList(@RequestBody Spec spec) {
        List<Spec> list = specService.findList(spec);
        return new Result<Spec>(true, StatusCode.OK, "搜索成功", list);
    }

    /***
     * Spec分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<Spec> findPage(@PathVariable int page, @PathVariable int size) {
        PageInfo<Spec> finds = specService.findpage(page, size);
        return new Result<Spec>(true, StatusCode.OK, "搜索成功", finds);
    }

    /***
     * Spec分页条件搜索实现
     * @param spec
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<Spec> findPage(@RequestBody(required = false) Spec spec, @PathVariable int page, @PathVariable int size) {
        PageInfo<Spec> pages = specService.findpage(spec, page, size);

        return new Result<Spec>(true, StatusCode.OK, "搜索成功", pages);
    }

    @GetMapping("/category/{categoryid}")
    public Result<List<Spec>> findByCategory(@PathVariable Integer categoryid) {
        List<Spec> specList = specService.findBycategoryid(categoryid);
        return new Result<List<Spec>>(true, StatusCode.OK, "分类id查询规格成功", specList);
    }
}
