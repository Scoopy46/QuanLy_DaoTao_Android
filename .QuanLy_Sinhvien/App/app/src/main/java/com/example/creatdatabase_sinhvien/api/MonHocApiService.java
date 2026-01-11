package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.MonHoc;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * API service interface cho các thao tác với MonHoc
 */
public interface MonHocApiService {
    @GET("api/MonHoc")
    Call<List<MonHoc>> getAllMonHoc();
}

