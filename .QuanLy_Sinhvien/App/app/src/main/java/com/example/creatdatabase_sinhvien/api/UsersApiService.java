package com.example.creatdatabase_sinhvien.api;

import com.example.creatdatabase_sinhvien.models.LoginRequest;
import com.example.creatdatabase_sinhvien.models.Users;
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
 * API service interface cho các thao tác với Users
 */
public interface UsersApiService {
    @POST("api/Users/login")
    Call<Users> login(@Body LoginRequest loginRequest);

    @GET("api/Users")
    Call<List<Users>> getAllUsers();

    @POST("api/Users")
    Call<Users> createUser(@Body Users user);

    @PUT("api/Users/{id}")
    Call<Void> updateUser(@Path("id") Integer id, @Body Users user);

    @DELETE("api/Users/{id}")
    Call<Void> deleteUser(@Path("id") Integer id);

    @GET("api/Users/chuacoaccount")
    Call<List<Users>> getChuaCoAccount();

    @POST("api/Users/ChangePassword")
    Call<Void> changePassword(
        @Query("user") String user,
        @Query("oldPass") String oldPass,
        @Query("newPass") String newPass
    );
}

