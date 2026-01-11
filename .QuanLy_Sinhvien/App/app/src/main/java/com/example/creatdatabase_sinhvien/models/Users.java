package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model đại diện cho thông tin Users
 */
public class Users {
    @SerializedName("userID")
    private Integer userID;
    
    @SerializedName("userName")
    private String userName;
    
    @SerializedName("password")
    private String password;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("maGV")
    private String maGV;
    
    @SerializedName("fullName")
    private String fullName;

    public Users() {
        this.userID = null;
        this.userName = "";
        this.password = "";
        this.type = "";
        this.maGV = "";
        this.fullName = "";
    }

    public Users(Integer userID, String userName, String password, String type, String maGV, String fullName) {
        this.userID = userID;
        this.userName = userName != null ? userName : "";
        this.password = password != null ? password : "";
        this.type = type != null ? type : "";
        this.maGV = maGV != null ? maGV : "";
        this.fullName = fullName != null ? fullName : "";
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null ? type : "";
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV != null ? maGV : "";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName != null ? fullName : "";
    }
}

