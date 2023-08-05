package com.example.book_novel.model;

public class JwtResponse {
    private String jwtToken;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
    public JwtResponse(String jwtToken,User user) {
        this.jwtToken = jwtToken;
        this.user = user;
    }
}
