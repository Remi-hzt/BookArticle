package com.example.book_novel.model;

import org.springframework.beans.factory.annotation.Value;

public class PageRequestData {
    @Value("0")
    private int page;
    @Value("5")
    private int size;

    public PageRequestData() {}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}