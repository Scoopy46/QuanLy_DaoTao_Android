package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;
import com.example.creatdatabase_sinhvien.api.LopApiService;
import com.example.creatdatabase_sinhvien.models.Lop;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý các thao tác với Lop
 */
public class LopRepository {
    private static final String TAG = "LopRepository";
    
    private final LopApiService apiService;
    private final Executor executor;

    public LopRepository() {
        this.apiService = RetrofitClient.getInstance().create(LopApiService.class);
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Callback interface cho các thao tác với danh sách lớp
     */
    public interface LopCallback {
        void onSuccess(List<Lop> lops);
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
     * Callback interface cho lấy lớp theo ID
     */
    public interface LopByIdCallback {
        void onSuccess(Lop lop);
        void onError(String error);
    }

    /**
     * Lấy tất cả lớp
     */
    public void getAllLop(LopCallback callback) {
        Log.d(TAG, "========== getAllLop START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Lop");
        Log.d(TAG, "Request method: GET");
        try {
            Call<List<Lop>> call = apiService.getAllLop();
            Log.d(TAG, "Call created successfully");
            Log.d(TAG, "Enqueueing call...");
            call.enqueue(new Callback<List<Lop>>() {
                @Override
                public void onResponse(Call<List<Lop>> call, Response<List<Lop>> response) {
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
                    
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Lop> lops = response.body();
                            Log.d(TAG, "Received " + lops.size() + " Lop from API");
                            Log.d(TAG, "Calling callback.onSuccess()...");
                            callback.onSuccess(lops);
                            Log.d(TAG, "callback.onSuccess() called successfully");
                        } else {
                            String error = "Lỗi: " + response.code() + " - " + response.message();
                            if (response.body() == null) {
                                error = "Không có dữ liệu trả về (Code: " + response.code() + ")";
                            }
                            Log.e(TAG, "Response error: " + error);
                            
                            // Đọc error body không block
                            try {
                                if (response.errorBody() != null) {
                                    // Clone errorBody để tránh consume nó
                                    okhttp3.ResponseBody errorBody = response.errorBody();
                                    if (errorBody != null) {
                                        String errorBodyString = errorBody.string();
                                        Log.e(TAG, "Error body: " + errorBodyString);
                                        if (errorBodyString != null && !errorBodyString.isEmpty()) {
                                            error += "\n" + errorBodyString;
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "Error body is null");
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error reading error body", e);
                            }
                            
                            Log.d(TAG, "Calling callback.onError()...");
                            callback.onError(error);
                            Log.d(TAG, "callback.onError() called successfully");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception in onResponse", e);
                        callback.onError("Lỗi xử lý response: " + e.getMessage());
                    }
                    Log.d(TAG, "========== Response handled ==========");
                }

                @Override
                public void onFailure(Call<List<Lop>> call, Throwable t) {
                    Log.e(TAG, "========== onFailure ==========");
                    try {
                        String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                        Log.e(TAG, "Error message: " + error);
                        try {
                            Log.e(TAG, "Request URL: " + (call.request() != null ? call.request().url() : "null"));
                        } catch (Exception e) {
                            Log.e(TAG, "Error getting request URL", e);
                        }
                        if (t.getCause() != null) {
                            Log.e(TAG, "Cause: " + t.getCause().getMessage(), t.getCause());
                        }
                        Log.e(TAG, "Full stack trace:", t);
                        callback.onError(error);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception in onFailure", e);
                        callback.onError("Lỗi: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception creating/enqueueing call", e);
            callback.onError("Lỗi: " + e.getMessage());
        }
        Log.d(TAG, "========== getAllLop END ==========");
    }

    /**
     * Lấy lớp theo ID
     */
    public void getLopById(String id, LopByIdCallback callback) {
        Call<Lop> call = apiService.getLopById(id);
        call.enqueue(new Callback<Lop>() {
            @Override
            public void onResponse(Call<Lop> call, Response<Lop> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String error = "Lỗi: " + response.code() + " - " + response.message();
                    Log.e(TAG, error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Lop> call, Throwable t) {
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Tạo lớp mới
     */
    public void createLop(Lop lop, OperationCallback callback) {
        Log.d(TAG, "========== createLop START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Lop");
        Log.d(TAG, "Request method: POST");
        Log.d(TAG, "Request body - maLop: " + lop.getMaLop());
        Log.d(TAG, "Request body - tenLop: " + lop.getTenLop());
        Log.d(TAG, "Request body - nienKhoa: " + lop.getNienKhoa());
        Log.d(TAG, "Request body - maNganh: " + lop.getMaNganh());
        Log.d(TAG, "Request body - tenNganh: " + lop.getTenNganh());
        Log.d(TAG, "Request body - tenKhoa: " + lop.getTenKhoa());
        
        Call<Lop> call = apiService.createLop(lop);
        call.enqueue(new Callback<Lop>() {
            @Override
            public void onResponse(Call<Lop> call, Response<Lop> response) {
                Log.d(TAG, "========== createLop Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Create success");
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
                Log.d(TAG, "========== createLop END ==========");
            }

            @Override
            public void onFailure(Call<Lop> call, Throwable t) {
                Log.e(TAG, "========== createLop onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Cập nhật lớp
     */
    public void updateLop(String id, Lop lop, OperationCallback callback) {
        Log.d(TAG, "========== updateLop START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Lop/" + id);
        Log.d(TAG, "Request method: PUT");
        Log.d(TAG, "Path parameter - id: " + id);
        Log.d(TAG, "Request body - maLop: " + lop.getMaLop());
        Log.d(TAG, "Request body - tenLop: " + lop.getTenLop());
        Log.d(TAG, "Request body - nienKhoa: " + lop.getNienKhoa());
        Log.d(TAG, "Request body - maNganh: " + lop.getMaNganh());
        Log.d(TAG, "Request body - tenNganh: " + lop.getTenNganh());
        Log.d(TAG, "Request body - tenKhoa: " + lop.getTenKhoa());
        
        Call<Void> call = apiService.updateLop(id, lop);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "========== updateLop Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Update success");
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
                Log.d(TAG, "========== updateLop END ==========");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "========== updateLop onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Xóa lớp
     */
    public void deleteLop(String id, OperationCallback callback) {
        Log.d(TAG, "Deleting Lop: " + id);
        Call<Void> call = apiService.deleteLop(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Delete success");
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
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }
}

