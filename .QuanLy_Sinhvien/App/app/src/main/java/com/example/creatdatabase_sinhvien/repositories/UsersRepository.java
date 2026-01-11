package com.example.creatdatabase_sinhvien.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.creatdatabase_sinhvien.MyApplication;
import com.example.creatdatabase_sinhvien.api.UsersApiService;
import com.example.creatdatabase_sinhvien.models.LoginRequest;
import com.example.creatdatabase_sinhvien.models.Users;
import com.example.creatdatabase_sinhvien.utils.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý các thao tác với Users
 */
public class UsersRepository {
    private static final String TAG = "UsersRepository";
    
    private final UsersApiService apiService;

    public UsersRepository() {
        this.apiService = RetrofitClient.getInstance().create(UsersApiService.class);
    }

    /**
     * Callback interface cho login
     */
    public interface LoginCallback {
        void onSuccess(Users user);
        void onError(String error);
    }

    /**
     * Callback interface cho các thao tác với danh sách users
     */
    public interface UsersCallback {
        void onSuccess(List<Users> users);
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
     * Callback interface cho lấy user theo ID
     */
    public interface UserByIdCallback {
        void onSuccess(Users user);
        void onError(String error);
    }

    /**
     * Đăng nhập
     */
    public void login(String userName, String password, LoginCallback callback) {
        Log.d(TAG, "========== login START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Users/login");
        Log.d(TAG, "Request method: POST");
        Log.d(TAG, "UserName: " + userName);
        
        LoginRequest loginRequest = new LoginRequest(userName, password);
        Call<Users> call = apiService.login(loginRequest);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Log.d(TAG, "========== login Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful() && response.body() != null) {
                    Users user = response.body();
                    Log.d(TAG, "Login success - UserID: " + user.getUserID());
                    Log.d(TAG, "FullName: " + user.getFullName());
                    
                    // Lấy token từ response header (nếu có)
                    // Kiểm tra nhiều header name phổ biến
                    String token = null;
                    
                    // Thử các header name phổ biến
                    String[] headerNames = {"Authorization", "Token", "X-Auth-Token", "token", "access_token", "accessToken"};
                    for (String headerName : headerNames) {
                        token = response.headers().get(headerName);
                        if (token != null && !token.isEmpty()) {
                            Log.d(TAG, "Token found in header: " + headerName);
                            break;
                        }
                    }
                    
                    if (token != null && !token.isEmpty()) {
                        // Loại bỏ "Bearer " nếu có
                        if (token.startsWith("Bearer ")) {
                            token = token.substring(7).trim();
                        }
                        Log.d(TAG, "========== Token received ==========");
                        Log.d(TAG, "Token (first 20 chars): " + (token.length() > 20 ? token.substring(0, 20) + "..." : token));
                        Log.d(TAG, "Token length: " + token.length());
                        
                        // Lưu token vào SharedPreferences
                        Context context = MyApplication.getInstance();
                        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("token", token);
                        editor.apply();
                        Log.d(TAG, "Token saved to SharedPreferences successfully");
                        Log.d(TAG, "========== Token saved ==========");
                    } else {
                        Log.w(TAG, "========== No token found ==========");
                        Log.w(TAG, "No token found in response headers");
                        // Log tất cả headers để debug
                        Log.d(TAG, "All response headers:");
                        for (String headerName : response.headers().names()) {
                            Log.d(TAG, "  " + headerName + ": " + response.headers().get(headerName));
                        }
                        Log.w(TAG, "Token will not be added to subsequent requests");
                    }
                    
                    callback.onSuccess(user);
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
                Log.d(TAG, "========== login END ==========");
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e(TAG, "========== login onFailure ==========");
                String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Lấy tất cả users
     */
    public void getAllUsers(UsersCallback callback) {
        Log.d(TAG, "========== getAllUsers START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Users");
        Log.d(TAG, "Request method: GET");
        
        Call<List<Users>> call = apiService.getAllUsers();
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                Log.d(TAG, "========== getAllUsers Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Users> users = response.body();
                    Log.d(TAG, "Received " + users.size() + " Users from API");
                    callback.onSuccess(users);
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
                Log.d(TAG, "========== getAllUsers END ==========");
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.e(TAG, "========== getAllUsers onFailure ==========");
                String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Tạo user mới
     */
    public void createUser(Users user, OperationCallback callback) {
        Log.d(TAG, "========== createUser START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Users");
        Log.d(TAG, "Request method: POST");
        Log.d(TAG, "UserName: " + user.getUserName());
        Log.d(TAG, "FullName: " + user.getFullName());
        Log.d(TAG, "Type: " + user.getType());
        Log.d(TAG, "MaGV: " + user.getMaGV());
        
        Call<Users> call = apiService.createUser(user);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Log.d(TAG, "========== createUser Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Create user success");
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
                Log.d(TAG, "========== createUser END ==========");
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e(TAG, "========== createUser onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Cập nhật user
     */
    public void updateUser(Integer id, Users user, OperationCallback callback) {
        Log.d(TAG, "========== updateUser START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Users/" + id);
        Log.d(TAG, "Request method: PUT");
        Log.d(TAG, "Path parameter - id: " + id);
        Log.d(TAG, "UserName: " + user.getUserName());
        Log.d(TAG, "FullName: " + user.getFullName());
        
        Call<Void> call = apiService.updateUser(id, user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "========== updateUser Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Update user success");
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
                Log.d(TAG, "========== updateUser END ==========");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "========== updateUser onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Xóa user
     */
    public void deleteUser(Integer id, OperationCallback callback) {
        Log.d(TAG, "========== deleteUser START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Users/" + id);
        Log.d(TAG, "Request method: DELETE");
        Log.d(TAG, "Path parameter - id: " + id);
        
        Call<Void> call = apiService.deleteUser(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "========== deleteUser Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Delete user success");
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
                Log.d(TAG, "========== deleteUser END ==========");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "========== deleteUser onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Lấy danh sách giáo viên chưa có account
     */
    public void getChuaCoAccount(UsersCallback callback) {
        Log.d(TAG, "========== getChuaCoAccount START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Users/chuacoaccount");
        Log.d(TAG, "Request method: GET");
        
        Call<List<Users>> call = apiService.getChuaCoAccount();
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                Log.d(TAG, "========== getChuaCoAccount Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Users> users = response.body();
                    Log.d(TAG, "Received " + users.size() + " Users chua co account");
                    callback.onSuccess(users);
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
                Log.d(TAG, "========== getChuaCoAccount END ==========");
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Log.e(TAG, "========== getChuaCoAccount onFailure ==========");
                String error = "Lỗi kết nối: " + (t.getMessage() != null ? t.getMessage() : t.toString());
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Đổi mật khẩu
     */
    public void changePassword(String user, String oldPass, String newPass, OperationCallback callback) {
        Log.d(TAG, "========== changePassword START ==========");
        Log.d(TAG, "API URL: https://nguyenha-001-site1.ltempurl.com/api/Users/ChangePassword");
        Log.d(TAG, "Request method: POST");
        Log.d(TAG, "User: " + user);
        Log.d(TAG, "OldPass: " + (oldPass != null ? "***" : "null"));
        Log.d(TAG, "NewPass: " + (newPass != null ? "***" : "null"));
        
        Call<Void> call = apiService.changePassword(user, oldPass, newPass);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "========== changePassword Response ==========");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Change password success");
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
                Log.d(TAG, "========== changePassword END ==========");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "========== changePassword onFailure ==========");
                String error = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }
}

