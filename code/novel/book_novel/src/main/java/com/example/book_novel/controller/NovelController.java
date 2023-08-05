package com.example.book_novel.controller;

import com.example.book_novel.model.Novel;
import com.example.book_novel.result.Result;
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
    private Result insertNovel(@RequestBody Novel novel){
        try {
            Boolean msg = novelService.insertNovel(novel);
            if(msg){
                return Result.success();
            }else {
                return Result.error();
            }
        }catch (Exception e){
            return Result.error();
        }
    }
    // 分页查询所有文章
    @GetMapping("/page")
    public Result getUsersByPage(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Novel> all = novelService.findAll(pageable);
        if(all!=null){
            return Result.success(all);
        }else {
            return Result.error();
        }
    }
    //删除文章
    @DeleteMapping("/deletenovel")
    public Result deleteNovel(@RequestBody Novel novel){
        try{
            String id = novel.getId();
            Boolean deletenovel=novelService.deleteNovelById(id);
            if(deletenovel){
                return Result.success();
            }else {
                return Result.error();
            }
        }catch (Exception e){
            return Result.error();
        }

    }
    //编辑文章
    @PutMapping("/updatenovel")
    public Result updateUser(@RequestBody Novel novel) {
        try {
            Boolean updateNovel=novelService.updateNovelById(novel);
            if(updateNovel){
                return Result.success();
            }else {
                return Result.error();
            }
        }catch (Exception e){
            return Result.error();
        }
    }
    //查询文章
    @PostMapping("/searchnovel")
    public Result SearchNovel(@RequestParam(required = false) String novelname,
                                   @RequestParam(required = false) String author,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size){
        try {
            Page<Novel> novels = novelService.SearchNovel(novelname, author, page, size);
            if(novels!=null){
                return Result.success(novels);
            }else {
                return Result.error();
            }
        }catch (Exception e){
            return Result.error();
        }

    }
}
