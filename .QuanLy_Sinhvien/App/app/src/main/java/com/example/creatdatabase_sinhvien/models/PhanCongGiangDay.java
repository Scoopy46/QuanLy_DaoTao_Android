package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model đại diện cho phân công giảng dạy (response GET).
 *
 * App hiển thị: tenMon, tenGV, soTinChi
 * App lưu ẩn: maMH, maGV, maLop để xử lý cập nhật/xóa.
 */
public class PhanCongGiangDay {
    @SerializedName("maMH")
    private String maMH;

    @SerializedName("maLop")
    private String maLop;

    @SerializedName("maGV")
    private String maGV;

    @SerializedName("tenMon")
    private String tenMon;

    @SerializedName("soTinChi")
    private Integer soTinChi;

    @SerializedName("tenLop")
    private String tenLop;

    @SerializedName("tenGV")
    private String tenGV;

    public PhanCongGiangDay() {
        this.maMH = "";
        this.maLop = "";
        this.maGV = "";
        this.tenMon = "";
        this.soTinChi = 0;
        this.tenLop = "";
        this.tenGV = "";
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH != null ? maMH : "";
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop != null ? maLop : "";
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV != null ? maGV : "";
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon != null ? tenMon : "";
    }

    public Integer getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(Integer soTinChi) {
        this.soTinChi = soTinChi != null ? soTinChi : 0;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop != null ? tenLop : "";
    }

    public String getTenGV() {
        return tenGV;
    }

    public void setTenGV(String tenGV) {
        this.tenGV = tenGV != null ? tenGV : "";
    }
}


