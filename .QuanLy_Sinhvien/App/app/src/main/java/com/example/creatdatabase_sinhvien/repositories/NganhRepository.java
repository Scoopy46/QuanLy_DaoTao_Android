package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;
import com.example.creatdatabase_sinhvien.api.NganhApiService;
import com.example.creatdatabase_sinhvien.models.Nganh;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý các thao tác với Nganh
 */
public class NganhRepository {
    private static final String TAG = "NganhRepository";
    
    private final NganhApiService apiService;
    private final Executor executor;

    public NganhRepository() {
        this.apiService = RetrofitClient.getInstance().create(NganhApiService.class);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Callback interface cho các thao tác với danh sách ngành
     */
    public interface NganhCallback {
        void onSuccess(List<Nganh> nganhs);
        void onError(String error);
    }

    /**
     * Lấy tất cả ngành
     */
    public void getAllNganh(NganhCallback callback) {
        Log.d(TAG, "Getting all Nganh");
        executor.execute(() -> {
            Call<List<Nganh>> call = apiService.getAllNganh();
            call.enqueue(new Callback<List<Nganh>>() {
                @Override
                public void onResponse(Call<List<Nganh>> call, Response<List<Nganh>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Get all Nganh success: " + response.body().size() + " items");
                        callback.onSuccess(response.body());
                    } else {
                        String error = "Lỗi: " + response.code() + " - " + response.message();
                        Log.e(TAG, error);
                        callback.onError(error);
                    }
                }

                @Override
                public void onFailure(Call<List<Nganh>> call, Throwable t) {
                    String error = "Lỗi kết nối: " + t.getMessage();
                    Log.e(TAG, error, t);
                    callback.onError(error);
                }
            });
        });
    }
}

