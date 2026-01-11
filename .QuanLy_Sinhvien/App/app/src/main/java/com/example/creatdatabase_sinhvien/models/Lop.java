package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model đại diện cho thông tin Lớp học
 */
public class Lop implements Serializable {
    @SerializedName("maLop")
    private String maLop;
    
    @SerializedName("tenLop")
    private String tenLop;
    
    @SerializedName("nienKhoa")
    private String nienKhoa;
    
    @SerializedName("maNganh")
    private String maNganh;
    
    @SerializedName("tenNganh")
    private String tenNganh; // Chỉ dùng để hiển thị, không lưu trong DB
    
    @SerializedName("tenKhoa")
    private String tenKhoa; // Backend yêu cầu trường này

    public Lop() {
        this.maLop = "";
        this.tenLop = "";
        this.nienKhoa = "";
        this.maNganh = "";
        this.tenNganh = "";
        this.tenKhoa = "";
    }

    public Lop(String maLop, String tenLop, String nienKhoa, String maNganh, String tenNganh, String tenKhoa) {
        this.maLop = maLop != null ? maLop : "";
        this.tenLop = tenLop != null ? tenLop : "";
        this.nienKhoa = nienKhoa != null ? nienKhoa : "";
        this.maNganh = maNganh != null ? maNganh : "";
        this.tenNganh = tenNganh != null ? tenNganh : "";
        this.tenKhoa = tenKhoa != null ? tenKhoa : "";
    }

    // Getters and Setters
    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop != null ? maLop : "";
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop != null ? tenLop : "";
    }

    public String getNienKhoa() {
        return nienKhoa;
    }

    public void setNienKhoa(String nienKhoa) {
        this.nienKhoa = nienKhoa != null ? nienKhoa : "";
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

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa != null ? tenKhoa : "";
    }
}

