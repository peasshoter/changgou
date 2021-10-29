package com.changgou.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping
    public Result<List<Brand>> findAll() {
        List<Brand> all = brandService.findAll();
        return new Result<>(true, StatusCode.OK, "查询成功", all);
    }

    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable Integer id) {
        Brand brand = brandService.findById(id);
        return new Result<>(true, StatusCode.OK, "查询id成功", brand);
    }

    @PostMapping
    public Result add(@RequestBody Brand brand) {
        brandService.add(brand);
        return new Result<>(true, StatusCode.OK, "添加成功");
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Brand brand) {
        brand.setId(id);
        brandService.update(brand);
        return new Result<>(true, StatusCode.OK, "修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        brandService.delete(id);
        return new Result<>(true, StatusCode.OK, "删除成功");
    }

    @PostMapping("/search")
    public Result<Brand> searchList(@RequestBody Brand brand) {
        List<Brand> list = brandService.findList(brand);
        return new Result<>(true, StatusCode.OK, "查询成功", list);
    }

    @GetMapping("/search/{page}/{Size}")
    public Result<PageInfo<Brand>> findpage(@PathVariable Integer page, @PathVariable Integer Size) {
        PageInfo<Brand> page1 = brandService.findPage(page, Size);

        return new Result<PageInfo<Brand>>(true, StatusCode.OK, "分页查询成功", page1);
    }

    @PostMapping("/search/{page}/{Size}")
    public Result<PageInfo<Brand>> findpage(@RequestBody Brand brand, @PathVariable Integer page, @PathVariable Integer Size) {
        PageInfo<Brand> page1 = brandService.findPage(brand, page, Size);

        return new Result<PageInfo<Brand>>(true, StatusCode.OK, "分页查询成功", page1);
    }

    @GetMapping("/category/{categoryid}")
    public Result<List<Brand>> findByCategory(@PathVariable Integer categoryid) {
        List<Brand> brandList = brandService.findByCategory(categoryid);
        return new Result<List<Brand>>(true, StatusCode.OK, "分类id查询品牌成功", brandList);
    }
}
