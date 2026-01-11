package com.example.creatdatabase_sinhvien.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;

/**
 * Màn hình Home - Quản lý đào tạo
 */
public class HomeActivity extends AppCompatActivity {
    private Button btnQuanLySinhVien, btnQuanLyGiaoVien, btnQuanLyLop, btnQuanLyNguoiDung, btnQuanLyNganh, btnQuanLyMonHoc, btnQuanLyKhoa;
    private Button btnLogin, btnLogout;
    private TextView txtUserInfo;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        
        initViews();
        setupListeners();
        updateLoginUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật UI khi quay lại màn hình
        updateLoginUI();
    }

    private void initViews() {
        btnQuanLySinhVien = findViewById(R.id.btnQuanLySinhVien);
        btnQuanLyGiaoVien = findViewById(R.id.btnQuanLyGiaoVien);
        btnQuanLyLop = findViewById(R.id.btnQuanLyLop);
        btnQuanLyNguoiDung = findViewById(R.id.btnQuanLyNguoiDung);
        btnQuanLyNganh = findViewById(R.id.btnQuanLyNganh);
        btnQuanLyMonHoc = findViewById(R.id.btnQuanLyMonHoc);
        btnQuanLyKhoa = findViewById(R.id.btnQuanLyKhoa);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogout = findViewById(R.id.btnLogout);
        txtUserInfo = findViewById(R.id.txtUserInfo);
    }

    private void setupListeners() {
        btnQuanLySinhVien.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnQuanLyGiaoVien.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GiaoVienListActivity.class);
            startActivity(intent);
        });

        btnQuanLyLop.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LopListActivity.class);
            startActivity(intent);
        });

        btnQuanLyNguoiDung.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UsersListActivity.class);
            startActivity(intent);
        });

        btnQuanLyNganh.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NganhListActivity.class);
            startActivity(intent);
        });

        btnQuanLyMonHoc.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MonHocListActivity.class);
            startActivity(intent);
        });

        btnQuanLyKhoa.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, KhoaListActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            // Xóa thông tin đăng nhập và token
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userName");
            editor.remove("userID");
            editor.remove("fullName");
            editor.remove("type");
            editor.remove("token"); // Xóa token
            editor.apply();
            
            // Reset Retrofit để tạo lại client không có token
            com.example.creatdatabase_sinhvien.utils.RetrofitClient.reset();
            
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            updateLoginUI();
        });
    }

    private void updateLoginUI() {
        String userName = sharedPreferences.getString("userName", null);
        String fullName = sharedPreferences.getString("fullName", null);
        String type = sharedPreferences.getString("type", null);

        if (userName != null && !userName.isEmpty()) {
            // Đã đăng nhập
            btnLogin.setVisibility(android.view.View.GONE);
            btnLogout.setVisibility(android.view.View.VISIBLE);
            
            String userInfo = "Xin chào: " + (fullName != null ? fullName : userName);
            if (type != null) {
                userInfo += " (" + type + ")";
            }
            txtUserInfo.setText(userInfo);
            txtUserInfo.setVisibility(android.view.View.VISIBLE);
        } else {
            // Chưa đăng nhập
            btnLogin.setVisibility(android.view.View.VISIBLE);
            btnLogout.setVisibility(android.view.View.GONE);
            txtUserInfo.setVisibility(android.view.View.GONE);
        }
    }
}

