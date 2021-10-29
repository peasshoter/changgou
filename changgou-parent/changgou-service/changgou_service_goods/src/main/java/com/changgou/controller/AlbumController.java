package com.changgou.controller;

import com.changgou.goods.pojo.Album;
import com.changgou.service.AlbumService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    /***
     * 查询Album全部数据
     * @return
     */
    @GetMapping
    public Result<Album> findAll() {
        List<Album> all = albumService.findAll();
        return new Result<Album>(true, StatusCode.OK, "查询所有成功", all);
    }

    /***
     * 根据ID查询Album数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Album> findById(@PathVariable Integer id) {
        Album byId = albumService.findById(id);
        return new Result<Album>(true, StatusCode.OK, "查询id成功", byId);
    }

    /***
     * 新增Album数据
     * @param album
     * @return
     */
    @PostMapping
    public Result<Album> add(@RequestBody Album album) {
        albumService.add(album);
        return new Result<Album>(true, StatusCode.OK, "添加成功");
    }

    /***
     * 修改Album数据
     * @param album
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result<Album> update(@PathVariable Long id, @RequestBody Album album) {
        album.setId(id);
        albumService.update(album);
        return new Result<Album>(true, StatusCode.OK, "更新成功");
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Album> del(@PathVariable int id) {
        albumService.delete(id);
        return new Result<Album>(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param album
     * @return
     */
    @PostMapping("/search")
    public Result<Album> findList(@RequestBody Album album) {
        List<Album> list = albumService.findList(album);
        return new Result<Album>(true, StatusCode.OK, "搜索成功", list);
    }

    /***
     * Album分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<Album> findPage(@PathVariable int page, @PathVariable int size) {
        PageInfo<Album> finds = albumService.findpage(page, size);
        return new Result<Album>(true, StatusCode.OK, "搜索成功", finds);
    }

    /***
     * Album分页条件搜索实现
     * @param album
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<Album> findPage(@RequestBody(required = false) Album album, @PathVariable int page, @PathVariable int size) {
        PageInfo<Album> pages = albumService.findpage(album, page, size);

        return new Result<Album>(true, StatusCode.OK, "搜索成功", pages);
    }
}
