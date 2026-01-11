package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.PhanCongGiangDay;
import com.example.creatdatabase_sinhvien.models.PhanCongGiangDayRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * API service interface cho module Phân công giảng dạy
 *
 * - GET: lấy danh sách theo lớp (maLop)
 * - POST: thêm mới / cập nhật (server sẽ xử lý theo PK maMH+maLop)
 * - DELETE: xóa theo (maLop, maMH)
 */
public interface PhanCongGiangDayApiService {
    @GET("api/PhanCongGiangDay")
    Call<List<PhanCongGiangDay>> getByLop(@Query("maLop") String maLop);

    @POST("api/PhanCongGiangDay")
    Call<Void> save(@Body PhanCongGiangDayRequest request);

    @DELETE("api/PhanCongGiangDay")
    Call<Void> delete(@Query("maLop") String maLop, @Query("maMH") String maMH);
}


