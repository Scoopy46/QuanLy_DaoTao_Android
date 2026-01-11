package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;
import com.example.creatdatabase_sinhvien.api.MonHocApiService;
import com.example.creatdatabase_sinhvien.models.MonHoc;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý các thao tác với MonHoc
 */
public class MonHocRepository {
    private static final String TAG = "MonHocRepository";
    
    private final MonHocApiService apiService;
    private final Executor executor;

    public MonHocRepository() {
        this.apiService = RetrofitClient.getInstance().create(MonHocApiService.class);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public interface MonHocCallback {
        void onSuccess(List<MonHoc> monHocList);
        void onError(String error);
    }

    public void getAllMonHoc(MonHocCallback callback) {
        Log.d(TAG, "========== getAllMonHoc START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/MonHoc");
        Log.d(TAG, "Request method: GET");
        try {
            Call<List<MonHoc>> call = apiService.getAllMonHoc();
            Log.d(TAG, "Call created successfully");
            Log.d(TAG, "Enqueueing call...");
            call.enqueue(new Callback<List<MonHoc>>() {
                @Override
                public void onResponse(Call<List<MonHoc>> call, Response<List<MonHoc>> response) {
                    Log.d(TAG, "========== Response received ==========");
                    try {
                        if (call.request() != null) {
                            Log.d(TAG, "Request URL: " + call.request().url());
                            Log.d(TAG, "Request method: " + call.request().method());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error getting request info", e);
                    }
                    Log.d(TAG, "Response code: " + response.code());
                    Log.d(TAG, "Response message: " + response.message());
                    try {
                        Log.d(TAG, "Response headers: " + response.headers());
                    } catch (Exception e) {
                        Log.e(TAG, "Error getting response headers", e);
                    }
                    Log.d(TAG, "Is successful: " + response.isSuccessful());
                    
                    if (response.code() == 403) {
                        Log.e(TAG, "ERROR 403 FORBIDDEN - Server từ chối truy cập");
                        Log.e(TAG, "Có thể do: thiếu authentication, CORS, hoặc quyền truy cập");
                    }
                    
                    if (response.isSuccessful() && response.body() != null) {
                        List<MonHoc> monHocs = response.body();
                        Log.d(TAG, "Get all mon hoc success: " + monHocs.size() + " items");
                        callback.onSuccess(monHocs);
                    } else {
                        String error = "Lỗi: " + response.code() + " - " + response.message();
                        if (response.body() == null) {
                            error = "Không có dữ liệu trả về (Code: " + response.code() + ")";
                        }
                        Log.e(TAG, "Response error: " + error);
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e(TAG, "Error body: " + errorBody);
                            } else {
                                Log.e(TAG, "Error body is null");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body", e);
                        }
                        callback.onError(error);
                    }
                    Log.d(TAG, "========== Response handled ==========");
                }

                @Override
                public void onFailure(Call<List<MonHoc>> call, Throwable t) {
                    Log.e(TAG, "========== onFailure ==========");
                    String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                    Log.e(TAG, "Error message: " + error);
                    try {
                        Log.e(TAG, "Request URL: " + (call.request() != null ? call.request().url() : "null"));
                    } catch (Exception e) {
                        Log.e(TAG, "Error getting request URL", e);
                    }
                    if (t.getCause() != null) {
                        Log.e(TAG, "Cause: " + t.getCause().getMessage(), t.getCause());
                    }
                    Log.e(TAG, "Full stack trace:", t);
                    callback.onError(error);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception creating/enqueueing call", e);
            callback.onError("Lỗi: " + e.getMessage());
        }
        Log.d(TAG, "========== getAllMonHoc END ==========");
    }
}

