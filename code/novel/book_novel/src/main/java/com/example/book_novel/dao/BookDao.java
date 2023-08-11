package com.example.book_novel.dao;

import com.example.book_novel.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDao extends MongoRepository<Book,String> {
    Page<Book> findAll(Pageable pageable);
}
