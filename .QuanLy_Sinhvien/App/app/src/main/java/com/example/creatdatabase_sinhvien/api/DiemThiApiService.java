package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.DiemThi;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * API service interface cho các thao tác với DiemThi
 */
public interface DiemThiApiService {
    @GET("api/DiemThi")
    Call<List<DiemThi>> getDiemThi(
        @Query("maLop") String maLop,
        @Query("maMH") String maMH
    );

    @POST("api/DiemThi")
    Call<DiemThi> createOrUpdateDiemThi(@Body DiemThi diemThi);
}

