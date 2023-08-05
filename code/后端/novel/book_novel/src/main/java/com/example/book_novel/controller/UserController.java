package com.example.book_novel.controller;

import com.example.book_novel.model.PageRequestData;
import com.example.book_novel.model.User;
import com.example.book_novel.result.Result;
import com.example.book_novel.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;

//用户管理
@RestController
@RequestMapping("/ap/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //查询所有用户
    @GetMapping (value = "/findAllUser" )
    public Result findAllUser(){
        List<User> userList = userService.findAllUser();
        return Result.success(userList);
    }
    // 分页查询所有用户
    @PostMapping("/page")
    public Result getUsersByPage(@RequestBody PageRequestData pageRequestData) {
        try {
            int page = pageRequestData.getPage();
            int size = pageRequestData.getSize();
            String key = "users:" + page + ":" + size;
            Pageable pagea = PageRequest.of(page, size);
            Page<User> pageable= userService.findAll(pagea);
            return Result.success(pageable);
        }catch (Exception e){
            return Result.error();
        }
    }
    //根据username查询
    @PostMapping("/findusername")
    public Result findByUsername(@RequestBody User user){
        try{
            String username = user.getUsername();
            User user1 = (User) redisTemplate.opsForValue().get(username);
            if (user1 != null) {
                System.out.println("获取缓存");
                return Result.success(user1);
            }else {
                user1 = userService.findByUsername(username);
                if(user1!=null){
                    redisTemplate.opsForValue().set(username, user1);
                    return Result.success(user1);
                }
                else {
                    return Result.error("用户不存在");
                }
            }
        }catch (Exception e){
            return Result.error();
        }
    }

    //编辑用户
    @PutMapping("/updateuser")
    public Result updateUser(@RequestBody User user) {
        try{
            String username=user.getUsername();
            String password=user.getPassword();
            String role=user.getRole();
            if(StringUtils.isAnyBlank(username,password,role)){
                return Result.error("错误不能为空");
            }
            else {
                if (("ROLE_ADMIN".equals(role) || "ROLE_USER".equals(role))) {
                    // role是"ROLE_ADMIN"或"ROLE_USER"的处理逻辑
                    // 创建bcrypt密码编码器
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    // 对密码进行bcrypt加密
                    String encryptedPassword = passwordEncoder.encode(password);
                    // 更新用户对象的密码为加密后的密码
                    password=encryptedPassword;
                    Boolean updateUser=userService.updateUser(username,password,role);
                    if(updateUser){
                        redisTemplate.opsForValue().getAndSet(username,user);
                        return Result.success();
                    }else {
                        return Result.error();
                    }
                }else {
                    return Result.error();
                }
            }
        }catch (Exception e){
            return Result.error();
        }
    }

    //删除用户
    @DeleteMapping("/deleteuser")
    public Result deleteUser(@RequestBody User user){
        try{
            String username=user.getUsername();
            Boolean deleteuser = userService.deleteUserByName(username);
            if(deleteuser){
                // 删除用户成功，同时删除 Redis 缓存中的用户信息
                redisTemplate.delete("username:" + username);
                return Result.success();
            }
            return Result.error();
        }catch (Exception e){
            return Result.error();
        }
    }

    //用户搜索
    @PostMapping("/searchuser")
    public Result seachUser(@RequestBody User user,@RequestBody PageRequestData pageRequestData){
        try {
            String username = user.getUsername();
            int page = pageRequestData.getPage();
            int size = pageRequestData.getSize();
            Page<User> users = userService.SearchUser(username, page, size);
            return Result.success(users);
        }catch (Exception e){
            return Result.error();
        }
    }

    //用户修改密码
    @PostMapping("updatepwd")
    public Result updatepwd(@RequestParam String username,@RequestParam String ordpassword,@RequestParam String newpassword){
        try {
            if(StringUtils.isAnyBlank(username,ordpassword,newpassword)){
                User pwdmatch=userService.pwdmatch(username,ordpassword);
                if(pwdmatch!=null){
                    // 创建bcrypt密码编码器
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    // 对密码进行bcrypt加密
                    String encryptedPassword = passwordEncoder.encode(newpassword);
                    // 更新用户对象的密码为加密后的密码
                    newpassword=encryptedPassword;
                    Boolean updateUser=userService.updateUser(username,newpassword,pwdmatch.getRole());
                    if(updateUser){
                        redisTemplate.opsForValue().getAndSet(username,pwdmatch);
                        return Result.success();
                    }else {
                        return Result.error();
                    }
                }
                return Result.error("旧密码错误");
            }else {
                return Result.error("错误不能为空");
            }
        }catch (Exception e){
            return Result.error();
        }
    }

}
