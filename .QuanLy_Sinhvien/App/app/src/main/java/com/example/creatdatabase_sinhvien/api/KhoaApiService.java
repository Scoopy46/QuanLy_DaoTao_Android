package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.Khoa;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * API service interface cho các thao tác với Khoa
 */
public interface KhoaApiService {
    @GET("api/Khoa")
    Call<List<Khoa>> getAllKhoa();

    @POST("api/Khoa")
    Call<Khoa> createKhoa(@Body Khoa khoa);

    @PUT("api/Khoa/{id}")
    Call<Void> updateKhoa(@Path("id") String id, @Body Khoa khoa);

    @DELETE("api/Khoa/{id}")
    Call<Void> deleteKhoa(@Path("id") String id);
}

