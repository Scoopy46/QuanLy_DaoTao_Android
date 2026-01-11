package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;

import com.example.creatdatabase_sinhvien.api.PhanCongChuNhiemApiService;
import com.example.creatdatabase_sinhvien.models.PhanCongChuNhiem;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository cho module Phân công Chủ nhiệm
 */
public class PhanCongChuNhiemRepository {
    private static final String TAG = "PCCNRepository";

    private final PhanCongChuNhiemApiService apiService;
    private final Executor executor;

    public PhanCongChuNhiemRepository() {
        this.apiService = RetrofitClient.getInstance().create(PhanCongChuNhiemApiService.class);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public interface ListCallback {
        void onSuccess(List<PhanCongChuNhiem> list);
        void onError(String error);
    }

    public interface OperationCallback {
        void onSuccess();
        void onError(String error);
    }

    public void getAll(ListCallback callback) {
        Log.d(TAG, "========== getAll START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/PhanCongChuNhiem");
        try {
            Call<List<PhanCongChuNhiem>> call = apiService.getAll();
            call.enqueue(new Callback<List<PhanCongChuNhiem>>() {
                @Override
                public void onResponse(Call<List<PhanCongChuNhiem>> call, Response<List<PhanCongChuNhiem>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        executor.execute(() -> callback.onSuccess(response.body()));
                    } else {
                        executor.execute(() -> callback.onError(buildError(response)));
                    }
                }

                @Override
                public void onFailure(Call<List<PhanCongChuNhiem>> call, Throwable t) {
                    String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                    Log.e(TAG, error, t);
                    executor.execute(() -> callback.onError(error));
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception getAll", e);
            callback.onError("Lỗi: " + e.getMessage());
        }
        Log.d(TAG, "========== getAll END ==========");
    }

    public void update(String maLop, String maGV, OperationCallback callback) {
        Log.d(TAG, "========== update START ==========");
        Log.d(TAG, "POST /api/PhanCongChuNhiem?maLop=" + maLop + "&maGV=" + maGV);
        try {
            Call<Void> call = apiService.update(maLop, maGV);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        executor.execute(callback::onSuccess);
                    } else {
                        executor.execute(() -> callback.onError(buildError(response)));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                    Log.e(TAG, error, t);
                    executor.execute(() -> callback.onError(error));
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception update", e);
            callback.onError("Lỗi: " + e.getMessage());
        }
        Log.d(TAG, "========== update END ==========");
    }

    private static String buildError(Response<?> response) {
        String error = "Lỗi: " + response.code() + " - " + response.message();
        try {
            ResponseBody body = response.errorBody();
            if (body != null) {
                String s = body.string();
                if (s != null && !s.isEmpty()) error += "\n" + s;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading error body", e);
        }
        return error;
    }
}


