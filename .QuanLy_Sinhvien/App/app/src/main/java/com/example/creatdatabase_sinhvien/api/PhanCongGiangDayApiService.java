package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.PhanCongGiangDay;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * API service interface cho module Phân công giảng dạy
 *
 * - GET: lấy danh sách theo lớp (maLop)
 * - POST: thêm mới / cập nhật (dùng query parameters: maLop, maMH, maGV)
 * - DELETE: xóa theo (maLop, maMH)
 * - GET GetLopDay: lấy danh sách lớp dạy của giáo viên (maGV)
 * - GET GetMonDay: lấy danh sách môn dạy của giáo viên trong lớp (maLop, maGV)
 */
public interface PhanCongGiangDayApiService {
    @GET("api/PhanCongGiangDay")
    Call<List<PhanCongGiangDay>> getByLop(@Query("maLop") String maLop);

    @POST("api/PhanCongGiangDay")
    Call<Void> save(@Query("maLop") String maLop, @Query("maMH") String maMH, @Query("maGV") String maGV);

    @DELETE("api/PhanCongGiangDay")
    Call<Void> delete(@Query("maLop") String maLop, @Query("maMH") String maMH);

    @GET("api/PhanCongGiangDay/GetLopDay")
    Call<List<PhanCongGiangDay>> getLopDay(@Query("maGV") String maGV);

    @GET("api/PhanCongGiangDay/GetMonDay")
    Call<List<PhanCongGiangDay>> getMonDay(@Query("maLop") String maLop, @Query("maGV") String maGV);
}


