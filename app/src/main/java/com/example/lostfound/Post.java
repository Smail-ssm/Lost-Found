package com.example.lostfound;

public class Post {

    private String title, user, description, phoneNum, userId, postId;

    public Post(){

    }

    public Post(String user, String title, String description, String phoneNum, String userId, String postId) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.phoneNum = phoneNum;
        this.userId = userId;
        this.postId = postId;
    }

    public String getUser(){
        return this.user;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getPostId() {
        return this.postId;
    }

}

