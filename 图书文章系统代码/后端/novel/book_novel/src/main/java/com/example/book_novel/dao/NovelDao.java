package com.example.book_novel.dao;

import com.example.book_novel.model.Novel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelDao extends MongoRepository<Novel,String> {
    Page<Novel> findAll(Pageable pageable);
    void deleteById(String id);

}
