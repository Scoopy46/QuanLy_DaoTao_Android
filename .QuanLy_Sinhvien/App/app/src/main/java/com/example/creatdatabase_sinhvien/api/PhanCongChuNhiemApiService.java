package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.PhanCongChuNhiem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * API service interface cho module Phân công Chủ nhiệm
 *
 * - GET: /api/PhanCongChuNhiem
 * - POST: /api/PhanCongChuNhiem?maLop=...&maGV=...
 */
public interface PhanCongChuNhiemApiService {
    @GET("api/PhanCongChuNhiem")
    Call<List<PhanCongChuNhiem>> getAll();

    @POST("api/PhanCongChuNhiem")
    Call<Void> update(@Query("maLop") String maLop, @Query("maGV") String maGV);
}


