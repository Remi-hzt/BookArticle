package com.example.book_novel.model;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection ="novel") //指定要对应的文档名（表名）
@Data
public class Novel {
    @Id
    private String id;
    private String novelname;
    private String novelbrief;
    private String author;
    private String opentime;
    private String content;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNovelname() {
        return novelname;
    }

    public void setNovelname(String novelname) {
        this.novelname = novelname;
    }

    public String getNovelbrief() {
        return novelbrief;
    }

    public void setNovelbrief(String novelbrief) {
        this.novelbrief = novelbrief;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOpentime(String formatDateTime) {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Novel{" +
                "id='" + id + '\'' +
                ", novelname='" + novelname + '\'' +
                ", novelbrief='" + novelbrief + '\'' +
                ", author='" + author + '\'' +
                ", opentime='" + opentime + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
