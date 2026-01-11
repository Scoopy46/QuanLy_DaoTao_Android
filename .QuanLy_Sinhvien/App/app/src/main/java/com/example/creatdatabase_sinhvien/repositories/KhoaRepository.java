package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;
import com.example.creatdatabase_sinhvien.api.KhoaApiService;
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý các thao tác với Khoa
 */
public class KhoaRepository {
    private static final String TAG = "KhoaRepository";
    
    private final KhoaApiService apiService;
    private final Executor executor;

    public KhoaRepository() {
        this.apiService = RetrofitClient.getInstance().create(KhoaApiService.class);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Callback interface cho các thao tác với danh sách khoa
     */
    public interface KhoaCallback {
        void onSuccess(List<Khoa> khoas);
        void onError(String error);
    }

    /**
     * Lấy tất cả khoa
     */
    public void getAllKhoa(KhoaCallback callback) {
        Call<List<Khoa>> call = apiService.getAllKhoa();
        call.enqueue(new Callback<List<Khoa>>() {
            @Override
            public void onResponse(Call<List<Khoa>> call, Response<List<Khoa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> callback.onSuccess(response.body()));
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    executor.execute(() -> callback.onError(error));
                }
            }

            @Override
            public void onFailure(Call<List<Khoa>> call, Throwable t) {
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                executor.execute(() -> callback.onError(error));
            }
        });
    }
}

