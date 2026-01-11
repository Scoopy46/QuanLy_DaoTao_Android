package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class GiaoVien implements Serializable {
    @SerializedName("maGV")
    private String maGV;
    
    @SerializedName("hoten")
    private String hoten;
    
    @SerializedName("sdt")
    private String sdt;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("maKhoa")
    private String maKhoa;
    
    @SerializedName("hocHam")
    private String hocHam;
    
    @SerializedName("hocVi")
    private String hocVi;
    
    @SerializedName("anh")
    private String anh;
    
    @SerializedName("tenKhoa")
    private String tenKhoa;
    
    @SerializedName("userID")
    private Integer userID;

    public GiaoVien() {
        this.maGV = "";
        this.hoten = "";
        this.sdt = "";
        this.email = "";
        this.maKhoa = "";
        this.hocHam = "";
        this.hocVi = "";
        this.anh = "";
        this.tenKhoa = "";
        this.userID = null;
    }

    public GiaoVien(String maGV, String hoten, String sdt, String email, 
                    String maKhoa, String hocHam, String hocVi, String anh, String tenKhoa) {
        this.maGV = maGV != null ? maGV : "";
        this.hoten = hoten != null ? hoten : "";
        this.sdt = sdt != null ? sdt : "";
        this.email = email != null ? email : "";
        this.maKhoa = maKhoa != null ? maKhoa : "";
        this.hocHam = hocHam != null ? hocHam : "";
        this.hocVi = hocVi != null ? hocVi : "";
        this.anh = anh != null ? anh : "";
        this.tenKhoa = tenKhoa != null ? tenKhoa : "";
        this.userID = null;
    }

    // Getters and Setters
    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV != null ? maGV : "";
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten != null ? hoten : "";
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt != null ? sdt : "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email : "";
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa != null ? maKhoa : "";
    }

    public String getHocHam() {
        return hocHam;
    }

    public void setHocHam(String hocHam) {
        this.hocHam = hocHam != null ? hocHam : "";
    }

    public String getHocVi() {
        return hocVi;
    }

    public void setHocVi(String hocVi) {
        this.hocVi = hocVi != null ? hocVi : "";
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh != null ? anh : "";
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa != null ? tenKhoa : "";
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        String name = hoten != null && !hoten.isEmpty() ? hoten : "";
        String id = maGV != null && !maGV.isEmpty() ? maGV : "";
        if (!name.isEmpty() && !id.isEmpty()) return id + " - " + name;
        return !name.isEmpty() ? name : id;
    }
}

