package com.changgou.service;

import com.changgou.content.pojo.Content;

import java.util.List;

public interface ContentService {
    List<Content> findByCategory(Long id);
}
