package com.example.book_novel.service;

import com.example.book_novel.model.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface BookService {
    //添加书籍
    Boolean insertBook(Book book, MultipartFile bookfile) throws IOException;
    //修改书记
    Boolean updateBook(String id,String bookname,String bookbrief,String author);
    //删除书籍
    Boolean deleteBook(String id,String fileId);
    //分页显示
    Page<Book> findAll(Pageable pageable);
    //下载书籍
    ResponseEntity<?> downloadFile(String fileId, String filename);
    //查询
    Page<Book> SearchBook(String bookname, String author, Integer page, Integer size);
}
