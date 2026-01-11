package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model đại diện cho thông tin môn học
 */
public class MonHoc {
    @SerializedName("maMH")
    private String maMH;
    
    @SerializedName("tenMon")
    private String tenMon;

    public MonHoc() {
        this.maMH = "";
        this.tenMon = "";
    }

    public MonHoc(String maMH, String tenMon) {
        this.maMH = maMH != null ? maMH : "";
        this.tenMon = tenMon != null ? tenMon : "";
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH != null ? maMH : "";
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon != null ? tenMon : "";
    }

    @Override
    public String toString() {
        return maMH + " - " + tenMon;
    }
}

