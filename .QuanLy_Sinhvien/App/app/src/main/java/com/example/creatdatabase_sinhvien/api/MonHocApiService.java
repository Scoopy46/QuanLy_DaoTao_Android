package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.MonHoc;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * API service interface cho các thao tác với MonHoc
 */
public interface MonHocApiService {
    @GET("api/MonHoc")
    Call<List<MonHoc>> getAllMonHoc();

    @POST("api/MonHoc")
    Call<MonHoc> createMonHoc(@Body MonHoc monHoc);

    @PUT("api/MonHoc/{id}")
    Call<Void> updateMonHoc(@Path("id") String id, @Body MonHoc monHoc);

    @DELETE("api/MonHoc/{id}")
    Call<Void> deleteMonHoc(@Path("id") String id);
}

