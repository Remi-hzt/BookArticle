package com.example.book_novel.controller;

import com.example.book_novel.model.JwtUtils;
import com.example.book_novel.service.impl.UserDetailsServiceImpl;
import com.example.book_novel.model.JwtResponse;
import com.example.book_novel.model.User;
import com.example.book_novel.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    public String insertUser(@RequestBody User user){

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        user.setId(id);
        // 创建bcrypt密码编码器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 对密码进行bcrypt加密
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        // 更新用户对象的密码为加密后的密码
        user.setPassword(encryptedPassword);
        String msg =userService.insertUser(user);
        return msg;

    }
    //用户登录
    @PostMapping (value = "/userlogin" )
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        System.out.println("用户登录");
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User loginuser=userService.loginUser(username,password);
        if (loginuser != null) {
            String jwt = JwtUtils.createJwt(loginuser.getUsername());
            JwtResponse response = new JwtResponse(jwt, loginuser);
            Map<String,String> map = new HashMap<>();
            map.put("token",jwt);
            redisTemplate.opsForValue().set("login:"+username, jwt);
            return ResponseEntity.ok(response);
        } else {
            // 密码错误或用户不存在，返回401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    //注销
    @GetMapping (value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("注销");
        // 手动注销用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }
    //忘记密码
    @PostMapping(value = "/forgetpwd")
    public String forgetpwd(@RequestParam("username") String username,@RequestParam("password") String password){
        User user = userService.findByUsername(username);
        if(user!=null){
            // 创建bcrypt密码编码器
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 对密码进行bcrypt加密
            String encryptedPassword = passwordEncoder.encode(password);
            // 更新用户对象的密码为加密后的密码
            password=encryptedPassword;
            Boolean forgetpwd=userService.forgetpwd(username,password);
            if(forgetpwd){
                return "成功";
            }else {
                return "失败";
            }
        }else {
            return "用户名不存在";
        }
    }
}
