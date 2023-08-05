package com.example.book_novel.dao;

import com.example.book_novel.model.User;
import com.example.book_novel.service.UserService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends MongoRepository<User,String> {
    User findByusername(String username);
    /*验证用户和密码*/
    User findByUsername(String username);
    Page<User> findAll(Pageable pageable);
    String deleteByUsername(String name);
}





