package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.GiaoVien;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * API service interface cho các thao tác với GiaoVien
 */
public interface GiaoVienApiService {
    @GET("api/GiaoVien")
    Call<List<GiaoVien>> getAllGiaoVien(@Query("maKhoa") String maKhoa);

    @GET("api/GiaoVien/{id}")
    Call<GiaoVien> getGiaoVienById(@Path("id") String id);

    @POST("api/GiaoVien")
    Call<GiaoVien> createGiaoVien(@Body GiaoVien giaoVien);

    @PUT("api/GiaoVien/{id}")
    Call<Void> updateGiaoVien(@Path("id") String id, @Body GiaoVien giaoVien);

    @DELETE("api/GiaoVien/{id}")
    Call<Void> deleteGiaoVien(@Path("id") String id);
}

