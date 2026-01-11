package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.Nganh;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * API service interface cho các thao tác với Nganh
 */
public interface NganhApiService {
    @GET("api/Nganh")
    Call<List<Nganh>> getAllNganh();
}

