package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;

import com.example.creatdatabase_sinhvien.api.PhanCongGiangDayApiService;
import com.example.creatdatabase_sinhvien.models.PhanCongGiangDay;
import com.example.creatdatabase_sinhvien.models.PhanCongGiangDayRequest;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository cho module Phân công giảng dạy
 */
public class PhanCongGiangDayRepository {
    private static final String TAG = "PCGDRepository";

    private final PhanCongGiangDayApiService apiService;
    private final Executor executor;

    public PhanCongGiangDayRepository() {
        this.apiService = RetrofitClient.getInstance().create(PhanCongGiangDayApiService.class);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public interface PhanCongCallback {
        void onSuccess(List<PhanCongGiangDay> list);
        void onError(String error);
    }

    public interface OperationCallback {
        void onSuccess();
        void onError(String error);
    }

    public void getByLop(String maLop, PhanCongCallback callback) {
        Log.d(TAG, "========== getByLop START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/PhanCongGiangDay?maLop=" + maLop);
        try {
            Call<List<PhanCongGiangDay>> call = apiService.getByLop(maLop);
            call.enqueue(new Callback<List<PhanCongGiangDay>>() {
                @Override
                public void onResponse(Call<List<PhanCongGiangDay>> call, Response<List<PhanCongGiangDay>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        executor.execute(() -> callback.onSuccess(response.body()));
                    } else {
                        String error = buildError(response);
                        executor.execute(() -> callback.onError(error));
                    }
                }

                @Override
                public void onFailure(Call<List<PhanCongGiangDay>> call, Throwable t) {
                    String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                    Log.e(TAG, error, t);
                    executor.execute(() -> callback.onError(error));
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception getByLop", e);
            callback.onError("Lỗi: " + e.getMessage());
        }
        Log.d(TAG, "========== getByLop END ==========");
    }

    public void save(PhanCongGiangDayRequest request, OperationCallback callback) {
        Log.d(TAG, "========== save START ==========");
        Log.d(TAG, "POST /api/PhanCongGiangDay body maLop=" + request.getMaLop()
                + ", maMH=" + request.getMaMH() + ", maGV=" + request.getMaGV());
        try {
            Call<Void> call = apiService.save(request);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        executor.execute(callback::onSuccess);
                    } else {
                        String error = buildError(response);
                        executor.execute(() -> callback.onError(error));
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
            Log.e(TAG, "Exception save", e);
            callback.onError("Lỗi: " + e.getMessage());
        }
        Log.d(TAG, "========== save END ==========");
    }

    public void delete(String maLop, String maMH, OperationCallback callback) {
        Log.d(TAG, "========== delete START ==========");
        Log.d(TAG, "DELETE /api/PhanCongGiangDay?maLop=" + maLop + "&maMH=" + maMH);
        try {
            Call<Void> call = apiService.delete(maLop, maMH);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        executor.execute(callback::onSuccess);
                    } else {
                        String error = buildError(response);
                        executor.execute(() -> callback.onError(error));
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
            Log.e(TAG, "Exception delete", e);
            callback.onError("Lỗi: " + e.getMessage());
        }
        Log.d(TAG, "========== delete END ==========");
    }

    private static String buildError(Response<?> response) {
        String error = "Lỗi: " + response.code() + " - " + response.message();
        try {
            ResponseBody body = response.errorBody();
            if (body != null) {
                String s = body.string();
                if (s != null && !s.isEmpty()) {
                    error += "\n" + s;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading error body", e);
        }
        return error;
    }
}


