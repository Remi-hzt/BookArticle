package com.example.book_novel.service;


import com.example.book_novel.model.Novel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface NovelService {
//    添加小说
    Boolean insertNovel(Novel novel);
//    删除小说
    Boolean deleteNovelById(String id);
//    修改小说
    Boolean updateNovelById(Novel novel);
//Boolean updateNovelById(Novel novel);
//    分页显示小说
    Page<Novel> findAll(Pageable pageable);
    //查询
    Page<Novel> SearchNovel(String novelname,String author,Integer page,Integer size);
   // long countSearchNovel(String novelname,String author,List opentime);
}
