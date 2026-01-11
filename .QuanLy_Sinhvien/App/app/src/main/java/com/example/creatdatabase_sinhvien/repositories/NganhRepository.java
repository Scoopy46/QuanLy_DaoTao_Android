package com.example.creatdatabase_sinhvien.repositories;

import android.util.Log;
import com.example.creatdatabase_sinhvien.api.NganhApiService;
import com.example.creatdatabase_sinhvien.models.Nganh;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý các thao tác với Nganh
 */
public class NganhRepository {
    private static final String TAG = "NganhRepository";
    
    private final NganhApiService apiService;

    public NganhRepository() {
        this.apiService = RetrofitClient.getInstance().create(NganhApiService.class);
    }

    /**
     * Callback interface cho các thao tác với danh sách ngành
     */
    public interface NganhCallback {
        void onSuccess(List<Nganh> nganhs);
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
     * Lấy tất cả ngành
     */
    public void getAllNganh(NganhCallback callback) {
        Log.d(TAG, "========== getAllNganh START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Nganh");
        Log.d(TAG, "Request method: GET");
        
        Call<List<Nganh>> call = apiService.getAllNganh();
        call.enqueue(new Callback<List<Nganh>>() {
            @Override
            public void onResponse(Call<List<Nganh>> call, Response<List<Nganh>> response) {
                Log.d(TAG, "========== getAllNganh Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Nganh> nganhs = response.body();
                    Log.d(TAG, "Get all Nganh success: " + nganhs.size() + " items");
                    callback.onSuccess(nganhs);
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
                Log.d(TAG, "========== getAllNganh END ==========");
            }

            @Override
            public void onFailure(Call<List<Nganh>> call, Throwable t) {
                Log.e(TAG, "========== getAllNganh onFailure ==========");
                String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Tạo ngành mới
     */
    public void createNganh(Nganh nganh, OperationCallback callback) {
        Log.d(TAG, "========== createNganh START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Nganh");
        Log.d(TAG, "Request method: POST");
        Log.d(TAG, "maNganh: " + nganh.getMaNganh());
        Log.d(TAG, "tenNganh: " + nganh.getTenNganh());
        Log.d(TAG, "maKhoa: " + nganh.getMaKhoa());
        
        Call<Nganh> call = apiService.createNganh(nganh);
        call.enqueue(new Callback<Nganh>() {
            @Override
            public void onResponse(Call<Nganh> call, Response<Nganh> response) {
                Log.d(TAG, "========== createNganh Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Create nganh success");
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
                Log.d(TAG, "========== createNganh END ==========");
            }

            @Override
            public void onFailure(Call<Nganh> call, Throwable t) {
                Log.e(TAG, "========== createNganh onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Cập nhật ngành
     */
    public void updateNganh(String id, Nganh nganh, OperationCallback callback) {
        Log.d(TAG, "========== updateNganh START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Nganh/" + id);
        Log.d(TAG, "Request method: PUT");
        Log.d(TAG, "Path parameter - id: " + id);
        Log.d(TAG, "maNganh: " + nganh.getMaNganh());
        Log.d(TAG, "tenNganh: " + nganh.getTenNganh());
        Log.d(TAG, "maKhoa: " + nganh.getMaKhoa());
        
        Call<Void> call = apiService.updateNganh(id, nganh);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "========== updateNganh Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Update nganh success");
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
                Log.d(TAG, "========== updateNganh END ==========");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "========== updateNganh onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Xóa ngành
     */
    public void deleteNganh(String id, OperationCallback callback) {
        Log.d(TAG, "========== deleteNganh START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Nganh/" + id);
        Log.d(TAG, "Request method: DELETE");
        Log.d(TAG, "Path parameter - id: " + id);
        
        Call<Void> call = apiService.deleteNganh(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "========== deleteNganh Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Delete nganh success");
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
                Log.d(TAG, "========== deleteNganh END ==========");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "========== deleteNganh onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }
}

