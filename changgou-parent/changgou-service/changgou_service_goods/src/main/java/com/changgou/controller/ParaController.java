package com.changgou.controller;

import com.changgou.goods.pojo.Para;
import com.changgou.service.ParaService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/para")
public class ParaController {
    @Autowired
    private ParaService paraService;

    /***
     * 查询Para全部数据
     * @return
     */
    @GetMapping
    public Result<Para> findAll() {
        List<Para> all = paraService.findAll();
        return new Result<Para>(true, StatusCode.OK, "查询所有成功", all);
    }

    /***
     * 根据ID查询Para数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Para> findById(@PathVariable Integer id) {
        Para byId = paraService.findById(id);
        return new Result<Para>(true, StatusCode.OK, "查询id成功", byId);
    }

    /***
     * 新增Para数据
     * @param para
     * @return
     */
    @PostMapping
    public Result<Para> add(@RequestBody Para para) {
        paraService.add(para);
        return new Result<Para>(true, StatusCode.OK, "添加成功");
    }

    /***
     * 修改Para数据
     * @param para
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Para> update(@PathVariable int id, @RequestBody Para para) {
        para.setId(id);
        paraService.update(para);
        return new Result<Para>(true, StatusCode.OK, "更新成功");
    }

    /***
     * 根据ID删除para数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Para> del(@PathVariable int id) {
        paraService.delete(id);
        return new Result<Para>(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索para数据
     * @param para
     * @return
     */
    @PostMapping("/search")
    public Result<Para> findList(@RequestBody Para para) {
        List<Para> list = paraService.findList(para);
        return new Result<Para>(true, StatusCode.OK, "搜索成功", list);
    }

    /***
     * Para分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<Para> findPage(@PathVariable int page, @PathVariable int size) {
        PageInfo<Para> finds = paraService.findpage(page, size);
        return new Result<Para>(true, StatusCode.OK, "搜索成功", finds);
    }

    /***
     * Para分页条件搜索实现
     * @param para
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<Para> findPage(@RequestBody(required = false) Para para, @PathVariable int page, @PathVariable int size) {
        PageInfo<Para> pages = paraService.findpage(para, page, size);

        return new Result<Para>(true, StatusCode.OK, "搜索成功", pages);
    }

    @GetMapping("/category/{categoryid}")
    public Result<List<Para>> findByCategory(@PathVariable Integer categoryid) {
        List<Para> paraList = paraService.findByCategory(categoryid);
        return new Result<List<Para>>(true, StatusCode.OK, "分类id查询参数成功", paraList);
    }
}
