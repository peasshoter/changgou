package com.changgou.controller;

import com.changgou.goods.pojo.Template;
import com.changgou.service.TemplateService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/template")
public class TemplateController {
    @Autowired
    private TemplateService templateService;

    /***
     * 查询Template全部数据
     * @return
     */
    @GetMapping
    public Result<Template> findAll() {
        List<Template> all = templateService.findAll();
        return new Result<Template>(true, StatusCode.OK, "查询所有成功", all);
    }

    /***
     * 根据ID查询Template数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Template> findById(@PathVariable Integer id) {
        Template byId = templateService.findById(id);
        return new Result<Template>(true, StatusCode.OK, "查询id成功", byId);
    }

    /***
     * 新增Template数据
     * @param template
     * @return
     */
    @PostMapping
    public Result<Template> add(@RequestBody Template template) {
        templateService.add(template);
        return new Result<Template>(true, StatusCode.OK, "添加成功");
    }

    /***
     * 修改Template数据
     * @param template
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Template> update(@PathVariable int id, @RequestBody Template template) {
        template.setId(id);
        templateService.update(template);
        return new Result<Template>(true, StatusCode.OK, "更新成功");
    }

    /***
     * 根据ID删除template数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Template> del(@PathVariable int id) {
        templateService.delete(id);
        return new Result<Template>(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索template数据
     * @param template
     * @return
     */
    @PostMapping("/search")
    public Result<Template> findList(@RequestBody Template template) {
        List<Template> list = templateService.findList(template);
        return new Result<Template>(true, StatusCode.OK, "搜索成功", list);
    }

    /***
     * Template分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<Template> findPage(@PathVariable int page, @PathVariable int size) {
        PageInfo<Template> finds = templateService.findpage(page, size);
        return new Result<Template>(true, StatusCode.OK, "搜索成功", finds);
    }

    /***
     * Template分页条件搜索实现
     * @param template
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<Template> findPage(@RequestBody(required = false) Template template, @PathVariable int page, @PathVariable int size) {
        PageInfo<Template> pages = templateService.findpage(template, page, size);

        return new Result<Template>(true, StatusCode.OK, "搜索成功", pages);
    }

    @GetMapping("/category/{categoryid}")
    public Result<List<Template>> findByCategory(@PathVariable Integer categoryid) {
        List<Template> templateList = templateService.findByCategory(categoryid);
        return new Result<List<Template>>(true, StatusCode.OK, "分类id查询模板成功", templateList);
    }
}
