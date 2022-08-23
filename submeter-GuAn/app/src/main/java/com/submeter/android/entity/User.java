package com.submeter.android.entity;

public class User {
    /*用户名*/
    private String username;

    /*用户id*/
    private String userId;

    /*性别*/
    private String sex;

    /*电话*/
    private String mobile;

    private String userImg;
    /*用户类型*/
    private String userType="1";

    public String getUserImg() {
        return userImg;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public String getSex() {
        return sex;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUserType() {
        return userType;
    }
}