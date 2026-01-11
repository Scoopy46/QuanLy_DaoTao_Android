package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model đại diện cho thông tin Khoa (Department)
 */
public class Khoa {
    @SerializedName("maKhoa")
    private String maKhoa;
    
    @SerializedName("tenKhoa")
    private String tenKhoa;

    public Khoa() {
        this.maKhoa = "";
        this.tenKhoa = "";
    }

    public Khoa(String maKhoa, String tenKhoa) {
        this.maKhoa = maKhoa != null ? maKhoa : "";
        this.tenKhoa = tenKhoa != null ? tenKhoa : "";
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa != null ? maKhoa : "";
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa != null ? tenKhoa : "";
    }

    @Override
    public String toString() {
        return tenKhoa;
    }
}

