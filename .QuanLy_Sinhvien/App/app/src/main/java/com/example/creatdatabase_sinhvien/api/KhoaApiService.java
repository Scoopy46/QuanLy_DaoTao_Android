package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.Khoa;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * API service interface cho các thao tác với Khoa
 */
public interface KhoaApiService {
    @GET("api/Khoa")
    Call<List<Khoa>> getAllKhoa();
}

