package com.example.book_novel.controller;

import com.example.book_novel.model.Novel;
import com.example.book_novel.service.NovelService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


//文章管理
@RestController
@RequestMapping("/ap/novel")
public class NovelController {
    @Autowired
    private NovelService novelService;
    //添加文章
    @PostMapping ("/insertNovel")
    private Boolean insertNovel(@RequestBody Novel novel){
        Boolean msg = novelService.insertNovel(novel);
        return msg;
    }
    // 分页查询所有文章
    @GetMapping("/page")
    public Page<Novel> getUsersByPage(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return novelService.findAll(pageable);
    }
    //删除文章
    @DeleteMapping("/deletenovel/{id}")
    public boolean deleteNovel(@PathVariable String id){
        Boolean deletenovel=novelService.deleteNovelById(id);
        return deletenovel;
    }
    //编辑文章
    @PutMapping("/updatenovel")
    public Boolean updateUser(@RequestBody Novel novel) {
        Boolean updateNovel=novelService.updateNovelById(novel);
        return updateNovel;
    }
    //查询文章
    @PostMapping("/searchnovel")
    public Page<Novel> SearchNovel(@RequestParam(required = false) String novelname,
                                   @RequestParam(required = false) String author,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size){
        return novelService.SearchNovel(novelname,author,page,size);
    }
}
