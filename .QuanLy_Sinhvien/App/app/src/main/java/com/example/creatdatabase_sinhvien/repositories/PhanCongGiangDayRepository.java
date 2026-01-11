package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;

import com.example.creatdatabase_sinhvien.api.PhanCongGiangDayApiService;
import com.example.creatdatabase_sinhvien.models.PhanCongGiangDay;
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

    public void save(String maLop, String maMH, String maGV, OperationCallback callback) {
        Log.d(TAG, "========== save START ==========");
        Log.d(TAG, "POST /api/PhanCongGiangDay");
        Log.d(TAG, "Query params - maLop: '" + maLop + "' (length: " + (maLop != null ? maLop.length() : 0) + ")");
        Log.d(TAG, "Query params - maMH: '" + maMH + "' (length: " + (maMH != null ? maMH.length() : 0) + ")");
        Log.d(TAG, "Query params - maGV: '" + maGV + "' (length: " + (maGV != null ? maGV.length() : 0) + ")");
        
        // Validate parameters
        if (maLop == null || maLop.trim().isEmpty()) {
            Log.e(TAG, "ERROR: maLop is null or empty!");
            callback.onError("Lỗi: Mã lớp không được để trống");
            return;
        }
        if (maMH == null || maMH.trim().isEmpty()) {
            Log.e(TAG, "ERROR: maMH is null or empty!");
            callback.onError("Lỗi: Mã môn học không được để trống");
            return;
        }
        if (maGV == null || maGV.trim().isEmpty()) {
            Log.e(TAG, "ERROR: maGV is null or empty!");
            callback.onError("Lỗi: Mã giáo viên không được để trống");
            return;
        }
        
        // Trim values
        maLop = maLop.trim();
        maMH = maMH.trim();
        maGV = maGV.trim();
        
        // Log final values before API call
        Log.d(TAG, "Final values before API call:");
        Log.d(TAG, "  maLop: '" + maLop + "' (length: " + maLop.length() + ", isEmpty: " + maLop.isEmpty() + ")");
        Log.d(TAG, "  maMH: '" + maMH + "' (length: " + maMH.length() + ", isEmpty: " + maMH.isEmpty() + ")");
        Log.d(TAG, "  maGV: '" + maGV + "' (length: " + maGV.length() + ", isEmpty: " + maGV.isEmpty() + ")");
        
        // Final validation after trim
        if (maLop.isEmpty() || maMH.isEmpty() || maGV.isEmpty()) {
            Log.e(TAG, "ERROR: One or more values are empty after trim!");
            callback.onError("Lỗi: Dữ liệu không hợp lệ");
            return;
        }
        
        try {
            // Log expected URL
            String expectedUrl = "POST /api/PhanCongGiangDay?maLop=" + maLop + "&maMH=" + maMH + "&maGV=" + maGV;
            Log.d(TAG, "Expected URL: " + expectedUrl);
            Log.d(TAG, "Calling API with parameters:");
            Log.d(TAG, "  maLop (type: " + (maLop != null ? maLop.getClass().getSimpleName() : "null") + "): '" + maLop + "' (isEmpty: " + (maLop != null ? maLop.isEmpty() : "N/A") + ")");
            Log.d(TAG, "  maMH (type: " + (maMH != null ? maMH.getClass().getSimpleName() : "null") + "): '" + maMH + "' (isEmpty: " + (maMH != null ? maMH.isEmpty() : "N/A") + ")");
            Log.d(TAG, "  maGV (type: " + (maGV != null ? maGV.getClass().getSimpleName() : "null") + "): '" + maGV + "' (isEmpty: " + (maGV != null ? maGV.isEmpty() : "N/A") + ")");
            
            Call<Void> call = apiService.save(maLop, maMH, maGV);
            
            // Log request info if available (synchronous check)
            try {
                // Note: call.request() might not be available until enqueued, but we try anyway
                Log.d(TAG, "Call created, will log actual URL in response callback");
            } catch (Exception e) {
                Log.w(TAG, "Could not log request info", e);
            }
            
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d(TAG, "========== save Response ==========");
                    try {
                        if (call.request() != null) {
                            Log.d(TAG, "Actual request URL that was sent: " + call.request().url());
                            Log.d(TAG, "Request method: " + call.request().method());
                        }
                    } catch (Exception e) {
                        Log.w(TAG, "Could not log request URL in response", e);
                    }
                    Log.d(TAG, "Response code: " + response.code());
                    Log.d(TAG, "Response message: " + response.message());
                    
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Save success");
                        callback.onSuccess();
                    } else {
                        String error = buildError(response);
                        Log.e(TAG, "Save failed: " + error);
                        callback.onError(error);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG, "========== save onFailure ==========");
                    try {
                        if (call.request() != null) {
                            Log.e(TAG, "Failed request URL: " + call.request().url());
                        }
                    } catch (Exception e) {
                        Log.w(TAG, "Could not log request URL in failure", e);
                    }
                    String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                    Log.e(TAG, error, t);
                    callback.onError(error);
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
                    Log.e(TAG, "========== save onFailure ==========");
                    try {
                        if (call.request() != null) {
                            Log.e(TAG, "Failed request URL: " + call.request().url());
                        }
                    } catch (Exception e) {
                        Log.w(TAG, "Could not log request URL in failure", e);
                    }
                    String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                    Log.e(TAG, error, t);
                    callback.onError(error);
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


