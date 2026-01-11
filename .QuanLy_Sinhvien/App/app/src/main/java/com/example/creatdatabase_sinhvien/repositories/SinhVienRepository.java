package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;
import com.example.creatdatabase_sinhvien.api.SinhVienApiService;
import com.example.creatdatabase_sinhvien.models.SinhVien;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý các thao tác với SinhVien
 */
public class SinhVienRepository {
    private static final String TAG = "SinhVienRepository";
    
    private final SinhVienApiService apiService;
    private final Executor executor;

    public SinhVienRepository() {
        this.apiService = RetrofitClient.getInstance().create(SinhVienApiService.class);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Callback interface cho các thao tác với danh sách sinh viên
     */
    public interface SinhVienCallback {
        void onSuccess(List<SinhVien> sinhViens);
        void onError(String error);
    }

    /**
     * Callback interface cho các thao tác đơn lẻ
     */
    public interface OperationCallback {
        void onSuccess();
        void onError(String error);
    }

    /**
     * Lấy tất cả sinh viên
     */
    public void getAllSinhVien(SinhVienCallback callback) {
        Log.d(TAG, "========== getAllSinhVien START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/SinhVien");
        Log.d(TAG, "Request method: GET");
        executor.execute(() -> {
            try {
                Call<List<SinhVien>> call = apiService.getAllSinhVien();
                Log.d(TAG, "Call created successfully");
                Log.d(TAG, "Enqueueing call...");
                call.enqueue(new Callback<List<SinhVien>>() {
                    @Override
                    public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {
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
                            Log.d(TAG, "Get all success: " + response.body().size() + " items");
                            callback.onSuccess(response.body());
                        } else {
                            String error = "Lỗi: " + response.code() + " - " + response.message();
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
                    public void onFailure(Call<List<SinhVien>> call, Throwable t) {
                        Log.e(TAG, "========== onFailure ==========");
                        String error = "Lỗi kết nối: " + t.getMessage();
                        Log.e(TAG, "Error message: " + error);
                        Log.e(TAG, "Request URL: " + (call.request() != null ? call.request().url() : "null"));
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
        });
        Log.d(TAG, "========== getAllSinhVien END ==========");
    }

    /**
     * Tạo mới sinh viên
     */
    public void createSinhVien(SinhVien sinhVien, OperationCallback callback) {
        executor.execute(() -> {
            apiService.createSinhVien(sinhVien).enqueue(new Callback<SinhVien>() {
                @Override
                public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Create success");
                        callback.onSuccess();
                    } else {
                        String error = "Lỗi: " + response.code() + " - " + response.message();
                        if (response.code() == 400) {
                            error = "Không thể tạo (có thể mã đã tồn tại)";
                        }
                        Log.e(TAG, error);
                        callback.onError(error);
                    }
                }

                @Override
                public void onFailure(Call<SinhVien> call, Throwable t) {
                    String error = "Lỗi kết nối: " + t.getMessage();
                    Log.e(TAG, error, t);
                    callback.onError(error);
                }
            });
        });
    }

    /**
     * Cập nhật sinh viên
     */
    public void updateSinhVien(String maSV, SinhVien sinhVien, OperationCallback callback) {
        executor.execute(() -> {
            apiService.updateSinhVien(maSV, sinhVien).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Update success");
                        callback.onSuccess();
                    } else {
                        String error = "Lỗi: " + response.code() + " - " + response.message();
                        if (response.code() == 404) {
                            error = "Không tìm thấy sinh viên";
                        }
                        Log.e(TAG, error);
                        callback.onError(error);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    String error = "Lỗi kết nối: " + t.getMessage();
                    Log.e(TAG, error, t);
                    callback.onError(error);
                }
            });
        });
    }

    /**
     * Xóa sinh viên
     */
    public void deleteSinhVien(String maSV, OperationCallback callback) {
        executor.execute(() -> {
            apiService.deleteSinhVien(maSV).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Delete success");
                        callback.onSuccess();
                    } else {
                        String error = "Lỗi: " + response.code() + " - " + response.message();
                        if (response.code() == 404) {
                            error = "Không tìm thấy sinh viên";
                        }
                        Log.e(TAG, error);
                        callback.onError(error);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    String error = "Lỗi kết nối: " + t.getMessage();
                    Log.e(TAG, error, t);
                    callback.onError(error);
                }
            });
        });
    }
}

