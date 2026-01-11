package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model đại diện cho thông tin Ngành
 */
public class Nganh implements Serializable {
    @SerializedName("maNganh")
    private String maNganh;
    
    @SerializedName("tenNganh")
    private String tenNganh;
    
    @SerializedName("maKhoa")
    private String maKhoa;
    
    @SerializedName("tenKhoa")
    private String tenKhoa; // Chỉ dùng để hiển thị, không lưu trong DB

    public Nganh() {
        this.maNganh = "";
        this.tenNganh = "";
        this.maKhoa = "";
        this.tenKhoa = "";
    }

    public Nganh(String maNganh, String tenNganh, String maKhoa) {
        this.maNganh = maNganh != null ? maNganh : "";
        this.tenNganh = tenNganh != null ? tenNganh : "";
        this.maKhoa = maKhoa != null ? maKhoa : "";
        this.tenKhoa = "";
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

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa != null ? tenKhoa : "";
    }

    @Override
    public String toString() {
        return tenNganh;
    }
}

