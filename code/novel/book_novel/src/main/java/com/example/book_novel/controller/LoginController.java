package com.example.book_novel.controller;

import com.example.book_novel.model.JwtUtils;
import com.example.book_novel.result.Result;
import com.example.book_novel.result.ResultCodeEnum;
import com.example.book_novel.service.impl.UserDetailsServiceImpl;
import com.example.book_novel.model.JwtResponse;
import com.example.book_novel.model.User;
import com.example.book_novel.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*用户登录和注册*/
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;
    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //用户注册
    @PostMapping (value = "/insertUser")
    public Result insertUser(@RequestBody User user){
        try {
            String role = user.getRole();
            String password = user.getPassword();
            String username = user.getUsername();
            if(StringUtils.isBlank(role)
                    || StringUtils.isBlank(username)
                    || StringUtils.isBlank(password)){
                return Result.error("错误，不能为空");
            }
            if (("ROLE_ADMIN".equals(role) || "ROLE_USER".equals(role))) {
                // role是"ROLE_ADMIN"或"ROLE_USER"的处理逻辑
                UUID uuid = UUID.randomUUID();
                String id = uuid.toString();
                user.setId(id);
                // 创建bcrypt密码编码器
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                // 对密码进行bcrypt加密
                String encryptedPassword = passwordEncoder.encode(password);
                // 更新用户对象的密码为加密后的密码
                user.setPassword(encryptedPassword);
                String msg =userService.insertUser(user);
                if(msg=="成功"){
                    return Result.success();
                }else {
                    return Result.error(msg);
                }
            }else {
                // role为异常值
                return Result.error();
            }

        }catch (Exception e){
            return Result.error();
        }

    }
    //用户登录
    @PostMapping (value = "/userlogin" )
    public Result loginUser(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        //AuthenticationManager authenticate进行用户认证
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
//        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        try {
            User loginuser=userService.loginUser(username,password);
            if (loginuser != null) {
                String jwt = JwtUtils.createJwt(loginuser.getUsername());
                JwtResponse response = new JwtResponse(jwt, loginuser);
                Map<String,String> map = new HashMap<>();
                map.put("token",jwt);
                redisTemplate.opsForValue().set("login:"+username, jwt);
                return Result.success(response);
            } else {
                // 密码错误或用户不存在
                return Result.error("密码错误或用户不存在");
            }
        }catch (Exception e){
            return Result.error();
        }

    }
    //注销
    @GetMapping (value = "/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        try{
            // 手动注销用户
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                return Result.success();
            }
            return Result.error("注销错误");
        }catch (Exception e){
            return Result.error();
        }

    }
    //忘记密码
    @PostMapping(value = "/forgetpwd")
    public Result forgetpwd(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        try{
            //判断是否为空
            if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
                return Result.error();
            }else {
                //判断用户是否存在
                User finduser = userService.findByUsername(username);
                if (finduser != null) {
                    // 创建bcrypt密码编码器
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    // 对密码进行bcrypt加密
                    String encryptedPassword = passwordEncoder.encode(password);
                    // 更新用户对象的密码为加密后的密码
                    password = encryptedPassword;
                    //修改密码
                    Boolean forgetpwd = userService.forgetpwd(username, password);
                    if (forgetpwd) {
                        return Result.success();
                    } else {
                        return Result.error();
                    }
                } else {
                    return Result.error("用户名不存在");
                }
            }
        }catch (Exception e){
            return Result.error();
        }
    }
}
