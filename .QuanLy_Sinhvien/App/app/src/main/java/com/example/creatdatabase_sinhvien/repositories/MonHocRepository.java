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
        executor.execute(() -> {
            apiService.getAllMonHoc().enqueue(new Callback<List<MonHoc>>() {
                @Override
                public void onResponse(Call<List<MonHoc>> call, Response<List<MonHoc>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Get all mon hoc success: " + response.body().size() + " items");
                        callback.onSuccess(response.body());
                    } else {
                        String error = "Lỗi: " + response.code() + " - " + response.message();
                        Log.e(TAG, error);
                        callback.onError(error);
                    }
                }

                @Override
                public void onFailure(Call<List<MonHoc>> call, Throwable t) {
                    String error = "Lỗi kết nối: " + t.getMessage();
                    Log.e(TAG, error, t);
                    callback.onError(error);
                }
            });
        });
    }
}

