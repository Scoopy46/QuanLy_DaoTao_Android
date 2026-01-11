package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model đại diện cho kết quả GET /api/PhanCongChuNhiem
 * (Danh sách lớp + trạng thái GVCN)
 */
public class PhanCongChuNhiem {
    @SerializedName("maLop")
    private String maLop;

    @SerializedName("tenLop")
    private String tenLop;

    @SerializedName("maGV")
    private String maGV; // có thể null

    @SerializedName("tenGV")
    private String tenGV;

    @SerializedName("ngayBatDau")
    private String ngayBatDau;

    public PhanCongChuNhiem() {
        this.maLop = "";
        this.tenLop = "";
        this.maGV = null;
        this.tenGV = "";
        this.ngayBatDau = "";
    }

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

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getTenGV() {
        return tenGV;
    }

    public void setTenGV(String tenGV) {
        this.tenGV = tenGV != null ? tenGV : "";
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau != null ? ngayBatDau : "";
    }
}


