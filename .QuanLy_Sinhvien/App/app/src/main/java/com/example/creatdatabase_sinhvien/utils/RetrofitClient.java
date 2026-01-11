package com.example.creatdatabase_sinhvien.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton Retrofit client factory
 */
public class RetrofitClient {
    // Sửa IP này thành IP của máy chạy API
    // Dùng HTTP (không phải HTTPS)
    private static final String BASE_URL = "https://nguyenha-001-site1.ltempurl.com/";
    private static Retrofit retrofit;

    private RetrofitClient() {
        // Private constructor để ngăn instantiation
    }

    /**
     * Lấy Retrofit instance (singleton)
     */
    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(NetworkUtils.createHttpClient()) // Dùng HTTP client đơn giản
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

