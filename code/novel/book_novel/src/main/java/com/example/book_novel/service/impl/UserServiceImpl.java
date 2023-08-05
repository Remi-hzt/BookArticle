package com.example.book_novel.service.impl;

import com.example.book_novel.dao.UserDao;

import com.example.book_novel.model.User;
import com.example.book_novel.model.UserCache;
import com.example.book_novel.service.UserService;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    //注入dao
    @Autowired
    private UserDao userDao;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //添加用户
    @Override
    public String insertUser(User user){
        String id = UUID.randomUUID().toString();
        user.setId(id);
        System.out.println(user);
        String username = user.getUsername();
        User loginUser=userDao.findByUsername(username);
        //用户名存在
        if(loginUser!=null){
            return "用户名存在";
        }else {
            User insertuser =userDao.insert(user);
            if (insertuser!=null) {
                return "成功";
            } else {
                return "错误";
            }
        }
    }

    //查找用户根据用户名
    @Override
    public User findByUsername(String username) {
        User finduser=userDao.findByusername(username);
        return finduser;
    }

    //修改用户
    @Override
    public Boolean updateUser(String username,String password,String role) {
        User loginUser=userDao.findByUsername(username);
        if(loginUser!=null){
            Query query = new Query(Criteria.where("username").is(username));
            Update update = new Update();
            update.set("password", password);
            update.set("role",role);
            UpdateResult updatepassword = mongoTemplate.updateFirst(query, update, User.class);
            if (updatepassword.getModifiedCount() > 0) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

    //登录验证
    @Override
    public User loginUser(String username,String password) {
        User user = null;
        Object o = redisTemplate.opsForValue().get(username);
        // 创建bcrypt密码编码器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (o != null && o instanceof User) {
            user = (User) o;
            System.out.println(user.getPassword());
            boolean matches = user.getPassword().equals(password);
            if(matches){
                return user;
            }
        }else {
            user = userDao.findByUsername(username);
            if(user!=null){
                boolean matches = passwordEncoder.matches(user.getPassword(),password);
                if(matches){
                    redisTemplate.opsForValue().set(username, user);
                    return user;
                }
            }
        }
        return null;
    }

    //查询全部用户
    @Override
    public List<User> findAllUser() {
        List<User> userList = userDao.findAll();
        return userList;
    }

    //分页查询
    @Override
    public Page<User> findAll(Pageable pageable) {
        Page<User> userList = userDao.findAll(pageable);
        return userList;
    }

    //删除用户
    @Override
    public Boolean deleteUserByName(String username) {
        String deleteuser = userDao.deleteByUsername(username);
        if(deleteuser!=null){
            return true;
        }else {
            return false;
        }
    }

    //分页查询用户根据用户名
    @Override
    public Page<User> SearchUser(String username, Integer page, Integer size) {
        Criteria criteria = new Criteria();
        if(StringUtils.isNotBlank(username)){
            criteria.and("username").regex(".*"+username+".*");
        }
        // 使用 Query 对象封装查询条件
        Query query = new Query();
        query.addCriteria(criteria);

        // 分页查询数据
        Pageable pageable = PageRequest.of(page, size);
        List<User> searchbook = mongoTemplate.find(query.with(pageable), User.class);

        // 查询总记录数，将查询结果封装到分页对象中
        Long count = mongoTemplate.count(query, User.class);
        return new PageImpl<>(searchbook, pageable, count);
    }

    //修改密码
    @Override
    public Boolean forgetpwd(String username, String password) {
        Query query = new Query(Criteria.where("username").is(username));
        Update update = new Update();
        update.set("password", password);
        UpdateResult updatepassword = mongoTemplate.updateFirst(query, update, User.class);
        if (updatepassword.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    //密码匹配
    @Override
    public User pwdmatch(String username, String password){
        User user = userDao.findByUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches =passwordEncoder.matches(password, user.getPassword());
        System.out.println(user.getPassword());
        if(matches){
            return user;
        }
        return null;
    }


}
