package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.Nganh;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * API service interface cho các thao tác với Nganh
 */
public interface NganhApiService {
    @GET("api/Nganh")
    Call<List<Nganh>> getAllNganh();

    @POST("api/Nganh")
    Call<Nganh> createNganh(@Body Nganh nganh);

    @PUT("api/Nganh/{id}")
    Call<Void> updateNganh(@Path("id") String id, @Body Nganh nganh);

    @DELETE("api/Nganh/{id}")
    Call<Void> deleteNganh(@Path("id") String id);
}

