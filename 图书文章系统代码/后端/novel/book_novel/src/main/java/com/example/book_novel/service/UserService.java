package com.example.book_novel.service;

import com.example.book_novel.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    /*添加用户*/
    String insertUser(User user);
    User findByUsername(String username);
    /*修改用户*/
    String updateUser(String username,String password,String role);
    /*验证用户和密码*/
    User loginUser(String username,String password);
    //查询所有用户
    List<User> findAllUser();
    Page<User> findAll(Pageable pageable);
    Boolean deleteUserByName(String username);
    Page<User> SearchUser(String username,Integer page,Integer size);
    //忘记密码
    Boolean forgetpwd(String username,String password);
    //修改密码
    User pwdmatch(String username, String password);
}
