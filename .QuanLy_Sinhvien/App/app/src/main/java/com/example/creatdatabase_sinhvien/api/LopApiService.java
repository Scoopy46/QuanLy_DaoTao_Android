package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.Lop;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * API service interface cho các thao tác với Lop
 */
public interface LopApiService {
    @GET("api/Lop")
    Call<List<Lop>> getAllLop();

    @GET("api/Lop/{id}")
    Call<Lop> getLopById(@Path("id") String id);

    @GET("api/Lop/chunhiem/{maGV}")
    Call<List<Lop>> getLopByChuNhiem(@Path("maGV") String maGV);

    @GET("api/Lop/nganh")
    Call<List<Lop>> getLopByNganh();

    @POST("api/Lop")
    Call<Lop> createLop(@Body Lop lop);

    @PUT("api/Lop/{id}")
    Call<Void> updateLop(@Path("id") String id, @Body Lop lop);

    @DELETE("api/Lop/{id}")
    Call<Void> deleteLop(@Path("id") String id);
}

