package com.example.book_novel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BookNovelApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookNovelApplication.class, args);
    }

}
