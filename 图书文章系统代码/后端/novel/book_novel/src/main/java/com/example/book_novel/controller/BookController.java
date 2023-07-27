package com.example.book_novel.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.book_novel.model.Book;
import com.example.book_novel.service.BookService;
import com.mongodb.client.gridfs.GridFSBucket;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;


@RestController
@RequestMapping("/ap/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Resource
    private GridFSBucket gridFSBucket;
    //新增图书和上传图书
    @PostMapping(value = "/insertbook")
    public Boolean insertbook(@RequestParam("book") String bookstring ,@RequestParam("bookfile") MultipartFile bookfile) throws IOException {
        Book book= JSONObject.parseObject(bookstring, Book.class);
        Boolean msg = bookService.insertBook(book,bookfile);
        return msg;
    }
    //图书分页显示
    @GetMapping("/page")
    public Page<Book> getUsersByPage(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookService.findAll(pageable);
    }
    //删除图书
    @PostMapping("/deletebook")
    public boolean deleteBook(@RequestParam("id") String id,@RequestParam("fileId") String fileId){
        Boolean deletebook=bookService.deleteBook(id,fileId);
        return deletebook;
    }
    //下载图书
    @PostMapping("/downloadBook")
    public ResponseEntity<?> downloadFile(@RequestParam("fileId") String fileId,
                                          @RequestParam("filename") String filename){
        ResponseEntity<?> downFile= bookService.downloadFile(fileId,filename);
        return downFile;
    }
    //修改图书
    @PutMapping("/updatebook")
    public boolean updateBook(@RequestBody Book book){
        String bookname=book.getBookname();
        String bookbrief=book.getBookbrief();
        String author=book.getAuthor();
        String id=book.getId();
        Boolean updatebook = bookService.updateBook(id,bookname,bookbrief,author);
        return updatebook;
    }
    //查询图书
    @PostMapping("/searchbook")
    public Page<Book> SearchNovel(@RequestParam(required = false) String bookname,
                                   @RequestParam(required = false) String author,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size){
        return bookService.SearchBook(bookname,author,page,size);
    }
}
