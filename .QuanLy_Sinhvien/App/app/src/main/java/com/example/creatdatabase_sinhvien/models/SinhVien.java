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
    
    @SerializedName("hodem")
    private String hodem;
    
    @SerializedName("ten")
    private String ten;
    
    @SerializedName("ngaySinh")
    private String ngaySinh;
    
    @SerializedName("gioiTinh")
    private String gioiTinh;
    
    @SerializedName("maLop")
    private String maLop;
    
    @SerializedName("anh")
    private String anh;
    
    @SerializedName("tenLop")
    private String tenLop; // Chỉ dùng để hiển thị

    public SinhVien() {
        this.maSV = "";
        this.hodem = "";
        this.ten = "";
        this.ngaySinh = "";
        this.gioiTinh = "";
        this.maLop = "";
        this.anh = "";
        this.tenLop = "";
    }

    public SinhVien(String maSV, String hodem, String ten, String ngaySinh, 
                    String gioiTinh, String maLop, String anh, String tenLop) {
        this.maSV = maSV != null ? maSV : "";
        this.hodem = hodem != null ? hodem : "";
        this.ten = ten != null ? ten : "";
        this.ngaySinh = ngaySinh != null ? ngaySinh : "";
        this.gioiTinh = gioiTinh != null ? gioiTinh : "";
        this.maLop = maLop != null ? maLop : "";
        this.anh = anh != null ? anh : "";
        this.tenLop = tenLop != null ? tenLop : "";
    }

    // Getters and Setters
    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV != null ? maSV : "";
    }

    public String getHodem() {
        return hodem;
    }

    public void setHodem(String hodem) {
        this.hodem = hodem != null ? hodem : "";
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten != null ? ten : "";
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh != null ? ngaySinh : "";
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh != null ? gioiTinh : "";
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop != null ? maLop : "";
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh != null ? anh : "";
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop != null ? tenLop : "";
    }

    // Helper methods để tương thích ngược với code cũ
    public String getHoTen() {
        return (hodem != null ? hodem : "") + " " + (ten != null ? ten : "").trim();
    }

    // Tương thích ngược: namSinh -> ngaySinh (lấy năm từ ngaySinh)
    public int getNamSinh() {
        if (ngaySinh != null && !ngaySinh.isEmpty()) {
            try {
                // Giả sử ngaySinh có format "YYYY-MM-DD" hoặc "DD/MM/YYYY"
                if (ngaySinh.contains("/")) {
                    String[] parts = ngaySinh.split("/");
                    if (parts.length >= 3) {
                        return Integer.parseInt(parts[2]);
                    }
                } else if (ngaySinh.contains("-")) {
                    String[] parts = ngaySinh.split("-");
                    if (parts.length >= 1) {
                        return Integer.parseInt(parts[0]);
                    }
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    // Tương thích ngược: lop -> maLop hoặc tenLop
    public String getLop() {
        return maLop != null && !maLop.isEmpty() ? maLop : (tenLop != null ? tenLop : "");
    }
}

