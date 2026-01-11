package com.example.creatdatabase_sinhvien.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

/**
 * Interceptor để tự động thêm token vào header của mọi request
 */
public class AuthInterceptor implements Interceptor {
    private static final String TAG = "AuthInterceptor";
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_AUTHORIZATION = "Authorization";
    
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        // Lấy token từ SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, null);
        
        Request.Builder requestBuilder = originalRequest.newBuilder();
        
        // Nếu có token, thêm vào header
        if (token != null && !token.isEmpty()) {
            // Format: "Bearer {token}" (chuẩn OAuth 2.0)
            // Nếu server yêu cầu format khác, có thể đổi thành chỉ "token" hoặc "Token {token}"
            String authHeader = "Bearer " + token;
            requestBuilder.addHeader(KEY_AUTHORIZATION, authHeader);
            Log.d(TAG, "========== AuthInterceptor ==========");
            Log.d(TAG, "Request URL: " + originalRequest.url());
            Log.d(TAG, "Token added to header: " + (token.length() > 20 ? token.substring(0, 20) + "..." : token));
            Log.d(TAG, "Header: Authorization: Bearer " + (token.length() > 20 ? token.substring(0, 20) + "..." : token));
        } else {
            Log.d(TAG, "========== AuthInterceptor ==========");
            Log.d(TAG, "Request URL: " + originalRequest.url());
            Log.d(TAG, "No token found in SharedPreferences, request without auth header");
        }
        
        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }
}

