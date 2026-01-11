package com.example.creatdatabase_sinhvien.models;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/**
 * Model đại diện cho thông tin sinh viên
 */
public class SinhVien {
    @NonNull
    @SerializedName("maSV")
    private String maSV;
    
    @SerializedName("hoTen")
    private String hoTen;
    
    @SerializedName("namSinh")
    private int namSinh;
    
    @SerializedName("lop")
    private String lop;
    
    @SerializedName("anh")
    private String anh;

    public SinhVien() {
        this.maSV = "";
        this.hoTen = "";
        this.lop = "";
        this.anh = "";
    }

    public SinhVien(String maSV, String hoTen, int namSinh, String lop, String anh) {
        this.maSV = maSV != null ? maSV : "";
        this.hoTen = hoTen != null ? hoTen : "";
        this.namSinh = namSinh;
        this.lop = lop != null ? lop : "";
        this.anh = anh != null ? anh : "";
    }

    // Getters and Setters
    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV != null ? maSV : "";
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen != null ? hoTen : "";
    }

    public int getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(int namSinh) {
        this.namSinh = namSinh;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop != null ? lop : "";
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh != null ? anh : "";
    }
}

