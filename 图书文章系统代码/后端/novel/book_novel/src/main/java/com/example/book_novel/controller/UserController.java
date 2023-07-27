package com.example.book_novel.controller;

import com.example.book_novel.model.User;
import com.example.book_novel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    public List<User> findAllUser(){
        List<User> userList = userService.findAllUser();
        return userList;
    }
    // 分页查询所有用户
    @GetMapping("/page")
    public Page<User> getUsersByPage(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {

        String key = "users:" + page + ":" + size;
        Pageable pagea = PageRequest.of(page, size);
        Page<User> pageable= userService.findAll(pagea);
        return pageable;

    }
    //根据username查询
    @GetMapping("/findusername/{username}")
    public User findByUsername(@PathVariable("username") String username){
        User user = (User) redisTemplate.opsForValue().get(username);
        if (user != null) {
            System.out.println("获取缓存");
            return user;
        }else {
            user = userService.findByUsername(username);
            redisTemplate.opsForValue().set(username, user);
            return user;
        }

    }

    //编辑用户
    @PutMapping("/updateuser")
    public String updateUser(@RequestBody User user) {
        String username=user.getUsername();
        String password=user.getPassword();
        String role=user.getRole();
        // 创建bcrypt密码编码器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 对密码进行bcrypt加密
        String encryptedPassword = passwordEncoder.encode(password);
        // 更新用户对象的密码为加密后的密码
        password=encryptedPassword;
        String updateUser=userService.updateUser(username,password,role);
        redisTemplate.opsForValue().getAndSet(username,user);
        return updateUser;
    }
    //删除用户
    @DeleteMapping("/deleteuser/{username}")
    public Boolean deleteUser(@PathVariable String username){
        Boolean deleteuser = userService.deleteUserByName(username);
        if(deleteuser){
            // 删除用户成功，同时删除 Redis 缓存中的用户信息
            redisTemplate.delete("username:" + username);
        }
        return deleteuser;
    }
    //用户搜索
    @PostMapping("/searchuser")
    public Page<User> seachUser(@RequestParam(required = false) String username,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size){
        return userService.SearchUser(username,page,size);
    }

    //用户修改密码
    @PostMapping("updatepwd")
    public String updatepwd(@RequestParam String username,@RequestParam String ordpassword,@RequestParam String newpassword){
        User pwdmatch=userService.pwdmatch(username,ordpassword);
        System.out.println(pwdmatch);
        if(pwdmatch!=null){
            // 创建bcrypt密码编码器
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 对密码进行bcrypt加密
            String encryptedPassword = passwordEncoder.encode(newpassword);
            // 更新用户对象的密码为加密后的密码
            newpassword=encryptedPassword;
            String updateUser=userService.updateUser(username,newpassword,pwdmatch.getRole());
            redisTemplate.opsForValue().getAndSet(username,pwdmatch);
            return updateUser;
        }
       return "旧密码错误";
    }

}
