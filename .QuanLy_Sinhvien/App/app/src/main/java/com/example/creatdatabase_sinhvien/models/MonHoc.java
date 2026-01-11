package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Model đại diện cho thông tin môn học
 */
public class MonHoc implements Serializable {
    @SerializedName("maMH")
    private String maMH;
    
    @SerializedName("tenMon")
    private String tenMon;
    
    @SerializedName("soTinChi")
    private Integer soTinChi;

    public MonHoc() {
        this.maMH = "";
        this.tenMon = "";
        this.soTinChi = 0;
    }

    public MonHoc(String maMH, String tenMon, Integer soTinChi) {
        this.maMH = maMH != null ? maMH : "";
        this.tenMon = tenMon != null ? tenMon : "";
        this.soTinChi = soTinChi != null ? soTinChi : 0;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH != null ? maMH : "";
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

    @Override
    public String toString() {
        return maMH + " - " + tenMon;
    }
}

