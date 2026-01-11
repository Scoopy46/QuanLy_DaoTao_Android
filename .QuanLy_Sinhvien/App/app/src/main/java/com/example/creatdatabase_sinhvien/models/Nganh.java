package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model đại diện cho thông tin Ngành
 */
public class Nganh {
    @SerializedName("maNganh")
    private String maNganh;
    
    @SerializedName("tenNganh")
    private String tenNganh;
    
    @SerializedName("maKhoa")
    private String maKhoa;

    public Nganh() {
        this.maNganh = "";
        this.tenNganh = "";
        this.maKhoa = "";
    }

    public Nganh(String maNganh, String tenNganh, String maKhoa) {
        this.maNganh = maNganh != null ? maNganh : "";
        this.tenNganh = tenNganh != null ? tenNganh : "";
        this.maKhoa = maKhoa != null ? maKhoa : "";
    }

    public String getMaNganh() {
        return maNganh;
    }

    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh != null ? maNganh : "";
    }

    public String getTenNganh() {
        return tenNganh;
    }

    public void setTenNganh(String tenNganh) {
        this.tenNganh = tenNganh != null ? tenNganh : "";
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa != null ? maKhoa : "";
    }

    @Override
    public String toString() {
        return tenNganh;
    }
}

