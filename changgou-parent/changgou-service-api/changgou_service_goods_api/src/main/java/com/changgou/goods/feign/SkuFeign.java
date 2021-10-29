package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "goods")
@RequestMapping("/sku")
public interface SkuFeign {
    @GetMapping
    Result<List<Sku>> findAll();

    @PostMapping(value = "/search")
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

    @GetMapping(value = "/{id}")
    public Result<Sku> findById(@PathVariable(value = "id", required = true) Long id);

    @PostMapping(value = "/decr/count")
    Result decrCount(@RequestParam(value = "username") String username);
}
