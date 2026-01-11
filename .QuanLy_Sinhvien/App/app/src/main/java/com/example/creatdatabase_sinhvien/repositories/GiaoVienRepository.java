package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;
import com.example.creatdatabase_sinhvien.api.GiaoVienApiService;
import com.example.creatdatabase_sinhvien.models.GiaoVien;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý các thao tác với GiaoVien
 */
public class GiaoVienRepository {
    private static final String TAG = "GiaoVienRepository";
    
    private final GiaoVienApiService apiService;
    private final Executor executor;

    public GiaoVienRepository() {
        this.apiService = RetrofitClient.getInstance().create(GiaoVienApiService.class);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Callback interface cho các thao tác với danh sách giáo viên
     */
    public interface GiaoVienCallback {
        void onSuccess(List<GiaoVien> giaoViens);
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
     * Callback interface cho lấy giáo viên theo ID
     */
    public interface GiaoVienByIdCallback {
        void onSuccess(GiaoVien giaoVien);
        void onError(String error);
    }

    /**
     * Lấy tất cả giáo viên (có thể lọc theo khoa)
     */
    public void getAllGiaoVien(String maKhoa, GiaoVienCallback callback) {
        Log.d(TAG, "========== getAllGiaoVien START ==========");
        Log.d(TAG, "Will filter by maKhoa: " + maKhoa + " on client side");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/GiaoVien");
        Log.d(TAG, "Request method: GET");
        try {
            Call<List<GiaoVien>> call = apiService.getAllGiaoVien(null);
            Log.d(TAG, "Call created successfully");
            Log.d(TAG, "Enqueueing call...");
            call.enqueue(new Callback<List<GiaoVien>>() {
                @Override
                public void onResponse(Call<List<GiaoVien>> call, Response<List<GiaoVien>> response) {
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
                        List<GiaoVien> giaoViens = response.body();
                        Log.d(TAG, "Received " + giaoViens.size() + " GiaoVien from API");
                        
                        // Filter theo maKhoa ở client side nếu có
                        List<GiaoVien> filteredList = giaoViens;
                        if (maKhoa != null && !maKhoa.isEmpty()) {
                            filteredList = new java.util.ArrayList<>();
                            for (GiaoVien gv : giaoViens) {
                                if (maKhoa.equals(gv.getMaKhoa())) {
                                    filteredList.add(gv);
                                }
                            }
                            Log.d(TAG, "Filtered to " + filteredList.size() + " GiaoVien for maKhoa: " + maKhoa);
                        }
                        
                        Log.d(TAG, "Calling callback.onSuccess()...");
                        try {
                            callback.onSuccess(filteredList);
                            Log.d(TAG, "callback.onSuccess() called successfully");
                        } catch (Exception e) {
                            Log.e(TAG, "Error in callback.onSuccess()", e);
                        }
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
                        Log.d(TAG, "Calling callback.onError()...");
                        try {
                            callback.onError(error);
                            Log.d(TAG, "callback.onError() called successfully");
                        } catch (Exception e) {
                            Log.e(TAG, "Error in callback.onError()", e);
                        }
                    }
                    Log.d(TAG, "========== Response handled ==========");
                }

                @Override
                public void onFailure(Call<List<GiaoVien>> call, Throwable t) {
                    Log.e(TAG, "========== onFailure ==========");
                    String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
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
        Log.d(TAG, "========== getAllGiaoVien END ==========");
    }

    /**
     * Lấy giáo viên theo ID
     */
    public void getGiaoVienById(String id, GiaoVienByIdCallback callback) {
        Call<GiaoVien> call = apiService.getGiaoVienById(id);
        call.enqueue(new Callback<GiaoVien>() {
            @Override
            public void onResponse(Call<GiaoVien> call, Response<GiaoVien> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> callback.onSuccess(response.body()));
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    executor.execute(() -> callback.onError(error));
                }
            }

            @Override
            public void onFailure(Call<GiaoVien> call, Throwable t) {
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                executor.execute(() -> callback.onError(error));
            }
        });
    }

    /**
     * Tạo giáo viên mới
     */
    public void createGiaoVien(GiaoVien giaoVien, OperationCallback callback) {
        Call<GiaoVien> call = apiService.createGiaoVien(giaoVien);
        call.enqueue(new Callback<GiaoVien>() {
            @Override
            public void onResponse(Call<GiaoVien> call, Response<GiaoVien> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> callback.onSuccess());
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    executor.execute(() -> callback.onError(error));
                }
            }

            @Override
            public void onFailure(Call<GiaoVien> call, Throwable t) {
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                executor.execute(() -> callback.onError(error));
            }
        });
    }

    /**
     * Cập nhật giáo viên
     */
    public void updateGiaoVien(String id, GiaoVien giaoVien, OperationCallback callback) {
        Call<Void> call = apiService.updateGiaoVien(id, giaoVien);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> callback.onSuccess());
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    executor.execute(() -> callback.onError(error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                executor.execute(() -> callback.onError(error));
            }
        });
    }

    /**
     * Xóa giáo viên
     */
    public void deleteGiaoVien(String id, OperationCallback callback) {
        Call<Void> call = apiService.deleteGiaoVien(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    executor.execute(() -> callback.onSuccess());
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    executor.execute(() -> callback.onError(error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                executor.execute(() -> callback.onError(error));
            }
        });
    }
}

