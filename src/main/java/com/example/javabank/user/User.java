package com.example.javabank.user;

public class User {
    private String username;
    private String password;
    private Integer userId;

    public User(String username, String password, Integer userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    public User(){

    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString(){
        return "{\nuserId: "+getUserId()+",\nusername: "+getUsername()+",\npassword: "+getPassword()+"\n}";
    }
}
