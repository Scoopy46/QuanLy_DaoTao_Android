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
        // Không khởi tạo giá trị mặc định
    }

    public PhanCongGiangDayRequest(String maLop, String maMH, String maGV) {
        // Trim và chỉ set nếu không empty
        // Lưu ý: Server yêu cầu các trường này không được null hoặc empty
        if (maLop != null) {
            String trimmed = maLop.trim();
            this.maLop = !trimmed.isEmpty() ? trimmed : null;
        } else {
            this.maLop = null;
        }
        
        if (maMH != null) {
            String trimmed = maMH.trim();
            this.maMH = !trimmed.isEmpty() ? trimmed : null;
        } else {
            this.maMH = null;
        }
        
        if (maGV != null) {
            String trimmed = maGV.trim();
            this.maGV = !trimmed.isEmpty() ? trimmed : null;
        } else {
            this.maGV = null;
        }
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = (maLop != null && !maLop.trim().isEmpty()) ? maLop.trim() : null;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = (maMH != null && !maMH.trim().isEmpty()) ? maMH.trim() : null;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = (maGV != null && !maGV.trim().isEmpty()) ? maGV.trim() : null;
    }
}


