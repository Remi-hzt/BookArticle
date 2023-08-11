package com.example.book_novel.service.impl;


import com.example.book_novel.dao.BookDao;
import com.example.book_novel.model.Book;

import com.example.book_novel.service.BookService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private  BookDao bookDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Resource
    private GridFSBucket gridFSBucket;
    //新增图书
    @PostMapping("/upload")
    @Override
    public Book insertBook(Book book,MultipartFile file) throws IOException {
        try{
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            // 将文件内容存储到 GridFS 中
            GridFSUploadStream uploadStream = gridFSBucket.openUploadStream(file.getOriginalFilename());
            InputStream inputStream = file.getInputStream();
            //修改图书文件名称
            //GridFSUploadStream uploadStream = gridFSBucket.openUploadStream(newFilename);
            //InputStream inputStream = file.getInputStream();
            byte[] bytes = new byte[1024];
            int read = 0;
            while ((read = inputStream.read(bytes)) != -1) {
                uploadStream.write(bytes, 0, read);
            }
            ObjectId fileId = uploadStream.getObjectId();
            uploadStream.close();
            // 存储文件基本信息到 MongoDB
            book.setFilename(file.getOriginalFilename());
            book.setFileId(fileId.toString());
            book.setFileType(file.getContentType());
            //图书id
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            book.setId(id);
            Book insertbook=mongoTemplate.insert(book, "book");
            if(insertbook!=null){
                return insertbook;
            }else{
                return null;
            }
        }catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
            return null;
        } finally{
            // Close the file stream
            try {
                file.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Boolean updateBook(String id,String bookname,String bookbrief,String author) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("bookname", bookname);
        update.set("bookbrief", bookbrief);
        update.set("author", author);
        UpdateResult updatebook = mongoTemplate.updateFirst(query, update, Book.class);
        if (updatebook.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteBook(String id,String fileId) {
        try{
            bookDao.deleteById(id);
            gridFSBucket.delete(new ObjectId(fileId));
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        Page<Book> BookList = bookDao.findAll(pageable);
        return BookList;
    }


    @Override
    public ResponseEntity<?> downloadFile(String fileId,String filename) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            gridFSBucket.downloadToStream(new ObjectId(fileId), baos);
            byte[] bytes = baos.toByteArray();
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(bytes));
            String originalFileName = filename;
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=UTF-8''" + URLEncoder.encode(originalFileName, "UTF-8").replace("+", "%20"));
            return ResponseEntity.ok().headers(headers).body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error downloading file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public Page<Book> SearchBook(String bookname, String author, Integer page, Integer size) {
        Criteria criteria = new Criteria();
        if(StringUtils.isNotBlank(bookname)){
            criteria.and("bookname").regex(".*"+bookname+".*");
        }
        if(StringUtils.isNotBlank(author)){
            criteria.and("author").regex(".*"+author+".*");
        }
        // 使用 Query 对象封装查询条件
        Query query = new Query();
        query.addCriteria(criteria);

        // 分页查询数据
        Pageable pageable = PageRequest.of(page, size);
        List<Book> searchbook = mongoTemplate.find(query.with(pageable), Book.class);

        // 查询总记录数，将查询结果封装到分页对象中
        Long count = mongoTemplate.count(query, Book.class);
        return new PageImpl<>(searchbook, pageable, count);
    }

}
