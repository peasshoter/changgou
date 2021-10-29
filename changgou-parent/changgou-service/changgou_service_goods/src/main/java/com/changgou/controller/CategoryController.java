package com.changgou.controller;

import com.changgou.goods.pojo.Category;
import com.changgou.service.CategoryService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /***
     * 查询Category全部数据
     * @return
     */
    @GetMapping
    public Result<Category> findAll() {
        List<Category> all = categoryService.findAll();
        return new Result<Category>(true, StatusCode.OK, "查询所有成功", all);
    }

    /***
     * 根据ID查询Category数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable Integer id) {
        Category byId = categoryService.findById(id);
        return new Result<Category>(true, StatusCode.OK, "查询id成功", byId);
    }

    /***
     * 根据父ID查询Category数据
     * @param parentid
     * @return
     */
    @GetMapping("/list/{parentid}")
    public Result<Category> findByParentId(@PathVariable Integer parentid) {
        List<Category> byParentId = categoryService.findByParentId(parentid);
        return new Result<>(true, StatusCode.OK, "查询id成功", byParentId);
    }

    /***
     * 新增Category数据
     * @param category
     * @return
     */
    @PostMapping
    public Result<Category> add(@RequestBody Category category) {
        categoryService.add(category);
        return new Result<Category>(true, StatusCode.OK, "添加成功");
    }

    /***
     * 修改Category数据
     * @param category
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Category> update(@PathVariable int id, @RequestBody Category category) {
        category.setId(id);
        categoryService.update(category);
        return new Result<Category>(true, StatusCode.OK, "更新成功");
    }

    /***
     * 根据ID删除category数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Category> del(@PathVariable int id) {
        categoryService.delete(id);
        return new Result<Category>(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索category数据
     * @param category
     * @return
     */
    @PostMapping("/search")
    public Result<Category> findList(@RequestBody Category category) {
        List<Category> list = categoryService.findList(category);
        return new Result<Category>(true, StatusCode.OK, "搜索成功", list);
    }

    /***
     * Category分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<Category> findPage(@PathVariable int page, @PathVariable int size) {
        PageInfo<Category> finds = categoryService.findpage(page, size);
        return new Result<Category>(true, StatusCode.OK, "搜索成功", finds);
    }

    /***
     * Category分页条件搜索实现
     * @param category
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<Category> findPage(@RequestBody(required = false) Category category, @PathVariable int page, @PathVariable int size) {
        PageInfo<Category> pages = categoryService.findpage(category, page, size);

        return new Result<Category>(true, StatusCode.OK, "搜索成功", pages);
    }
}
