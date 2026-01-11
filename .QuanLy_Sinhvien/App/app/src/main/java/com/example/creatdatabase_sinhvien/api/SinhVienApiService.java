package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.SinhVien;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * API service interface cho các thao tác với SinhVien
 */
public interface SinhVienApiService {
    @GET("api/SinhVien")
    Call<List<SinhVien>> getAllSinhVien();

    @GET("api/SinhVien/{id}")
    Call<SinhVien> getSinhVienById(@Path("id") String id);

    @POST("api/SinhVien")
    Call<SinhVien> createSinhVien(@Body SinhVien sinhVien);

    @PUT("api/SinhVien/{id}")
    Call<Void> updateSinhVien(@Path("id") String id, @Body SinhVien sinhVien);

    @DELETE("api/SinhVien/{id}")
    Call<Void> deleteSinhVien(@Path("id") String id);
}

