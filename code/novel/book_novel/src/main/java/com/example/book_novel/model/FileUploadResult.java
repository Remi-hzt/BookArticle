package com.example.book_novel.model;

public class FileUploadResult {
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FileUploadResult{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

