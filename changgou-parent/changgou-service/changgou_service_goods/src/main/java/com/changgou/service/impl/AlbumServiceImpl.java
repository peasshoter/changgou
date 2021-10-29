package com.changgou.service.impl;

import com.changgou.dao.AlbumMapper;
import com.changgou.goods.pojo.Album;
import com.changgou.service.AlbumService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumMapper albumMapper;

    private Example example(Album album) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if (album.getTitle() != null && !"".equals(album.getTitle())) {
            criteria.andLike("title", "%" + album.getTitle() + "%");
        }
        if (album.getId() != null && !"".equals(album.getId())) {
            criteria.andEqualTo("id", album.getId());
        }
        if (album.getImage() != null && !"".equals(album.getImage())) {
            criteria.andEqualTo("image", album.getImage());
        }
        if (album.getImageItems() != null && !"".equals(album.getImageItems())) {
            criteria.andEqualTo("imageItems", album.getImageItems());
        }
        return example;
    }

    @Override
    public PageInfo<Album> findpage(Album album, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = example(album);
        List<Album> albums = albumMapper.selectByExample(example);
        return new PageInfo<>(albums);
    }

    @Override
    public PageInfo<Album> findpage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Album> albums = albumMapper.selectAll();
        return new PageInfo<>(albums);
    }

    @Override
    public List<Album> findList(Album album) {
        Example example = example(album);
        return albumMapper.selectByExample(example);
    }

    @Override
    public void delete(int id) {
        albumMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Album album) {
        albumMapper.updateByPrimaryKeySelective(album);
    }

    @Override
    public void add(Album album) {
        albumMapper.insert(album);
    }

    @Override
    public Album findById(Integer id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }
}
