package com.example.creatdatabase_sinhvien.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request body cho POST /api/PhanCongGiangDay
 * Chỉ gửi 3 mã ID: maLop, maMH, maGV
 */
public class PhanCongGiangDayRequest {
    @SerializedName("maLop")
    private String maLop;

    @SerializedName("maMH")
    private String maMH;

    @SerializedName("maGV")
    private String maGV;

    public PhanCongGiangDayRequest() {
        this.maLop = "";
        this.maMH = "";
        this.maGV = "";
    }

    public PhanCongGiangDayRequest(String maLop, String maMH, String maGV) {
        this.maLop = maLop != null ? maLop : "";
        this.maMH = maMH != null ? maMH : "";
        this.maGV = maGV != null ? maGV : "";
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop != null ? maLop : "";
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH != null ? maMH : "";
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV != null ? maGV : "";
    }
}


