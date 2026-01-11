package com.example.creatdatabase_sinhvien.utils;

import android.content.Context;
import com.example.creatdatabase_sinhvien.MyApplication;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton Retrofit client factory
 */
public class RetrofitClient {
    // Dùng HTTPS
    private static final String BASE_URL = "https://nguyenha-001-site1.ltempurl.com/";
    private static Retrofit retrofit;

    private RetrofitClient() {
        // Private constructor để ngăn instantiation
    }

    /**
     * Lấy Retrofit instance (singleton) với AuthInterceptor
     */
    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    Context context = MyApplication.getInstance();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(NetworkUtils.createUnsafeOkHttpClient(context)) // Dùng HTTPS với SSL trust all và AuthInterceptor
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * Reset Retrofit instance (dùng cho testing hoặc thay đổi base URL)
     */
    public static void reset() {
        retrofit = null;
    }
}

