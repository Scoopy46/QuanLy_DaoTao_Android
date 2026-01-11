package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model đại diện cho thông tin điểm thi
 */
public class DiemThi {
    @SerializedName("maSV")
    private String maSV;
    
    @SerializedName("maMH")
    private String maMH;
    
    @SerializedName("diemLan1")
    private Double diemLan1;
    
    @SerializedName("diemLan2")
    private Double diemLan2;
    
    @SerializedName("hoTen")
    private String hoTen;
    
    @SerializedName("tenMon")
    private String tenMon;

    public DiemThi() {
        this.maSV = "";
        this.maMH = "";
        this.hoTen = "";
        this.tenMon = "";
    }

    public DiemThi(String maSV, String maMH, Double diemLan1, Double diemLan2) {
        this.maSV = maSV != null ? maSV : "";
        this.maMH = maMH != null ? maMH : "";
        this.diemLan1 = diemLan1;
        this.diemLan2 = diemLan2;
    }

    // Getters and Setters
    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV != null ? maSV : "";
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH != null ? maMH : "";
    }

    public Double getDiemLan1() {
        return diemLan1;
    }

    public void setDiemLan1(Double diemLan1) {
        this.diemLan1 = diemLan1;
    }

    public Double getDiemLan2() {
        return diemLan2;
    }

    public void setDiemLan2(Double diemLan2) {
        this.diemLan2 = diemLan2;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen != null ? hoTen : "";
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon != null ? tenMon : "";
    }
}

