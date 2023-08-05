package com.example.book_novel.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.book_novel.model.Book;
import com.example.book_novel.result.Result;
import com.example.book_novel.service.BookService;
import com.mongodb.client.gridfs.GridFSBucket;

import org.apache.commons.lang3.StringUtils;
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
    //@RequestParam("book") String bookstring ,@RequestParam("bookfile") MultipartFile bookfile
    public Result insertbook(@RequestParam("bookname") String bookname, @RequestParam("bookbrief") String bookbrief,
                             @RequestParam("author") String author, @RequestParam("filename") String filename,
                             @RequestParam("file") MultipartFile file) throws IOException {
        try{
            if((StringUtils.isAnyBlank(bookname,bookbrief,author,filename) || file.isEmpty())){
                return Result.error("错误不能为空");
            }else{

                Book book= new Book();
                book.setBookname(bookname);
                book.setBookbrief(bookbrief);
                book.setAuthor(author);
                book.setFilename(filename);
                Book insertBook = bookService.insertBook(book,file);
                if(insertBook!=null){
                    return Result.success(insertBook);
                }else {
                    return Result.error();
                }
            }
        }catch (Exception e){
            return Result.error();
        }
    }

    //图书分页显示
    @GetMapping("/page")
    public Result getUsersByPage(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> serviceAll = bookService.findAll(pageable);
        return Result.success(serviceAll);
    }

    //删除图书
    @DeleteMapping("/deletebook")
    public Result deleteBook(@RequestBody Book book){
        String id = book.getId();
        String fileId = book.getFileId();
        try{
            if(StringUtils.isAnyBlank(id,fileId)){
                return Result.error("错误不能为空");
            }else {
                Boolean deletebook = bookService.deleteBook(id,fileId);
                if(deletebook){
                    return Result.success();
                }else {
                    return Result.error();
                }
            }
        }catch (Exception e){
            return Result.error();
        }
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
    public Result updateBook(@RequestBody Book book){
        try{
            String bookname=book.getBookname();
            String bookbrief=book.getBookbrief();
            String author=book.getAuthor();
            String id=book.getId();
            if(StringUtils.isAnyBlank(bookname,bookbrief,author,id)){
                return Result.error("错误不能为空");
            }else {
                Boolean updatebook = bookService.updateBook(id,bookname,bookbrief,author);
                if(updatebook){
                    return Result.success();
                }else {
                    return Result.error();
                }
            }
        }catch (Exception e){
            return Result.error();
        }

    }
    //查询图书
    @PostMapping("/searchbook")
    public Result SearchNovel(@RequestParam(required = false) String bookname,
                                   @RequestParam(required = false) String author,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size){
        Page<Book> books = bookService.SearchBook(bookname, author, page, size);
        return Result.success(books);
    }
}
