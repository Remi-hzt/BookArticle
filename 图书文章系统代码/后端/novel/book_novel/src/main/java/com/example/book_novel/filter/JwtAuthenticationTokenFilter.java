//package com.example.book_novel.filter;
//
//import com.example.book_novel.model.JwtUtils;
//import com.example.book_novel.model.User;
//import io.jsonwebtoken.Claims;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        //获取token
//        String token = request.getHeader("token");
//        String username="";
//        if(token==null){
//            //放行
//            filterChain.doFilter(request,response);
//            return;
//        }
//        //解析token
//        try {
//           Claims claims= JwtUtils.parseJwt(token);
//            username=claims.getSubject();
//        } catch (Exception e) {
//            // 令牌签名验证失败
//            e.printStackTrace();
//            throw new RuntimeException("token非法");
//        }
//        //从redis获取
//        String rediskey="login:"+username;
//        Object loginUser = redisTemplate.opsForValue().get(rediskey);
//        if(loginUser==null){
//            throw new RuntimeException("用户未登录");
//        }
//
//
//        filterChain.doFilter(request, response);
//    }
//}
