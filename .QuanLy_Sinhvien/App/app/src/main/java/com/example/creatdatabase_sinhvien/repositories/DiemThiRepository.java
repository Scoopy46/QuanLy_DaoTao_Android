package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;
import com.example.creatdatabase_sinhvien.api.DiemThiApiService;
import com.example.creatdatabase_sinhvien.models.DiemThi;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý các thao tác với DiemThi
 */
public class DiemThiRepository {
    private static final String TAG = "DiemThiRepository";
    
    private final DiemThiApiService apiService;
    private final Executor executor;

    public DiemThiRepository() {
        this.apiService = RetrofitClient.getInstance().create(DiemThiApiService.class);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public interface DiemThiCallback {
        void onSuccess(List<DiemThi> diemThiList);
        void onError(String error);
    }

    public interface OperationCallback {
        void onSuccess();
        void onError(String error);
    }

    public void getDiemThi(String lop, String maMH, DiemThiCallback callback) {
        executor.execute(() -> {
            apiService.getDiemThi(lop, maMH).enqueue(new Callback<List<DiemThi>>() {
                @Override
                public void onResponse(Call<List<DiemThi>> call, Response<List<DiemThi>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Get diem thi success: " + response.body().size() + " items");
                        callback.onSuccess(response.body());
                    } else {
                        String error = "Lỗi: " + response.code() + " - " + response.message();
                        Log.e(TAG, error);
                        callback.onError(error);
                    }
                }

                @Override
                public void onFailure(Call<List<DiemThi>> call, Throwable t) {
                    String error = "Lỗi kết nối: " + t.getMessage();
                    Log.e(TAG, error, t);
                    callback.onError(error);
                }
            });
        });
    }

    public void createOrUpdateDiemThi(DiemThi diemThi, OperationCallback callback) {
        executor.execute(() -> {
            apiService.createOrUpdateDiemThi(diemThi).enqueue(new Callback<DiemThi>() {
                @Override
                public void onResponse(Call<DiemThi> call, Response<DiemThi> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Create/Update diem thi success");
                        callback.onSuccess();
                    } else {
                        String error = "Lỗi: " + response.code() + " - " + response.message();
                        Log.e(TAG, error);
                        callback.onError(error);
                    }
                }

                @Override
                public void onFailure(Call<DiemThi> call, Throwable t) {
                    String error = "Lỗi kết nối: " + t.getMessage();
                    Log.e(TAG, error, t);
                    callback.onError(error);
                }
            });
        });
    }
}

