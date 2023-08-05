package com.example.book_novel.model;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtBuilder;
import java.util.Date;
public class JwtUtils {
    private static final long EXPIRATION_TIME = 3600000; // 过期时间 1 小时
    private static final String SECRET = "my_secret_key_123";
    public static Claims parseJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims;
        } catch (SignatureException e) {
            // 令牌签名验证失败
            return null;
        }
    }
    public static String createJwt(String username) {
        // 设置签名算法和密钥
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        byte[] signingKey = SECRET.getBytes();

        // 设置其他声明属性
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME * 1000))
                .signWith(signatureAlgorithm, signingKey);

        // 构建JWT并返回
        return builder.compact();
    }
}
