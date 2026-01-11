package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model đại diện cho request đăng nhập
 */
public class LoginRequest {
    @SerializedName("userName")
    private String userName;
    
    @SerializedName("password")
    private String password;

    public LoginRequest() {
        this.userName = "";
        this.password = "";
    }

    public LoginRequest(String userName, String password) {
        this.userName = userName != null ? userName : "";
        this.password = password != null ? password : "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName != null ? userName : "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password != null ? password : "";
    }
}

