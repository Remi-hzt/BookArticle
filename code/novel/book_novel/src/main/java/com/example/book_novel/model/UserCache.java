package com.example.book_novel.model;

public class UserCache {
    private String username;
    private User user;

    public UserCache(String username, User user) {

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
