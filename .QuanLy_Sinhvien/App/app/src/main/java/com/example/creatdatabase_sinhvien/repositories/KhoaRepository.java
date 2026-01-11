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
                    callback.onSuccess(response.body());
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<List<Khoa>> call, Throwable t) {
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Callback interface cho các thao tác đơn lẻ
     */
    public interface OperationCallback {
        void onSuccess();
        void onError(String error);
    }

    /**
     * Tạo khoa mới
     */
    public void createKhoa(Khoa khoa, OperationCallback callback) {
        Log.d(TAG, "========== createKhoa START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Khoa");
        Log.d(TAG, "Request method: POST");
        Log.d(TAG, "maKhoa: " + khoa.getMaKhoa());
        Log.d(TAG, "tenKhoa: " + khoa.getTenKhoa());
        
        Call<Khoa> call = apiService.createKhoa(khoa);
        call.enqueue(new Callback<Khoa>() {
            @Override
            public void onResponse(Call<Khoa> call, Response<Khoa> response) {
                Log.d(TAG, "========== createKhoa Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Create khoa success");
                    callback.onSuccess();
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            error += "\n" + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    callback.onError(error);
                }
                Log.d(TAG, "========== createKhoa END ==========");
            }

            @Override
            public void onFailure(Call<Khoa> call, Throwable t) {
                Log.e(TAG, "========== createKhoa onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Cập nhật khoa
     */
    public void updateKhoa(String id, Khoa khoa, OperationCallback callback) {
        Log.d(TAG, "========== updateKhoa START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Khoa/" + id);
        Log.d(TAG, "Request method: PUT");
        Log.d(TAG, "Path parameter - id: " + id);
        Log.d(TAG, "maKhoa: " + khoa.getMaKhoa());
        Log.d(TAG, "tenKhoa: " + khoa.getTenKhoa());
        
        Call<Void> call = apiService.updateKhoa(id, khoa);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "========== updateKhoa Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Update khoa success");
                    callback.onSuccess();
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            error += "\n" + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    callback.onError(error);
                }
                Log.d(TAG, "========== updateKhoa END ==========");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "========== updateKhoa onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Xóa khoa
     */
    public void deleteKhoa(String id, OperationCallback callback) {
        Log.d(TAG, "========== deleteKhoa START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Khoa/" + id);
        Log.d(TAG, "Request method: DELETE");
        Log.d(TAG, "Path parameter - id: " + id);
        
        Call<Void> call = apiService.deleteKhoa(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "========== deleteKhoa Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Delete khoa success");
                    callback.onSuccess();
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                            error += "\n" + errorBody;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    callback.onError(error);
                }
                Log.d(TAG, "========== deleteKhoa END ==========");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "========== deleteKhoa onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }
}

