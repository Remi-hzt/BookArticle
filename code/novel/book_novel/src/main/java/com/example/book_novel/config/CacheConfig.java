//package com.example.book_novel.config;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoOperations;
//
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//
//@Configuration
//@EnableCaching
//public class CacheConfig {
//    // 创建 RedisCacheManager Bean
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        return RedisCacheManager.builder(redisConnectionFactory).build();
//    }
//
//    // 创建 MongoCacheManager Bean
//    @Bean
//    public MongoCacheManager mongoCacheManager(MongoOperations mongoOperations) {
//        return new MongoCacheManager(mongoOperations);
//    }
//}
