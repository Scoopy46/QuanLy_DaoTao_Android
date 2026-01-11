package com.example.creatdatabase_sinhvien.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.creatdatabase_sinhvien.R;

/**
 * Màn hình Home - Quản lý đào tạo
 */
public class HomeActivity extends AppCompatActivity {
    private Button btnQuanLySinhVien, btnQuanLyGiaoVien, btnQuanLyLop, btnQuanLyNguoiDung, btnQuanLyNganh, btnQuanLyMonHoc, btnQuanLyKhoa, btnPhanCongGiangDay, btnPhanCongChuNhiem;
    private Button btnLogin, btnLogout;
    private TextView txtUserInfo, txtDangNhap;
    private CardView cardLopHoc, cardSinhVien, cardNganh, cardMonHoc, cardGiaoVien, cardNguoiDung, cardKhoa, cardPhanCongGiangDay, cardPhanCongChuNhiem;
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
        // Hidden buttons for backward compatibility
        btnQuanLySinhVien = findViewById(R.id.btnQuanLySinhVien);
        btnQuanLyGiaoVien = findViewById(R.id.btnQuanLyGiaoVien);
        btnQuanLyLop = findViewById(R.id.btnQuanLyLop);
        btnQuanLyNguoiDung = findViewById(R.id.btnQuanLyNguoiDung);
        btnQuanLyNganh = findViewById(R.id.btnQuanLyNganh);
        btnQuanLyMonHoc = findViewById(R.id.btnQuanLyMonHoc);
        btnQuanLyKhoa = findViewById(R.id.btnQuanLyKhoa);
        btnPhanCongGiangDay = findViewById(R.id.btnPhanCongGiangDay);
        btnPhanCongChuNhiem = findViewById(R.id.btnPhanCongChuNhiem);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogout = findViewById(R.id.btnLogout);
        txtUserInfo = findViewById(R.id.txtUserInfo);
        
        // New CardViews
        cardLopHoc = findViewById(R.id.cardLopHoc);
        cardSinhVien = findViewById(R.id.cardSinhVien);
        cardNganh = findViewById(R.id.cardNganh);
        cardMonHoc = findViewById(R.id.cardMonHoc);
        cardGiaoVien = findViewById(R.id.cardGiaoVien);
        cardNguoiDung = findViewById(R.id.cardNguoiDung);
        cardKhoa = findViewById(R.id.cardKhoa);
        cardPhanCongGiangDay = findViewById(R.id.cardPhanCongGiangDay);
        cardPhanCongChuNhiem = findViewById(R.id.cardPhanCongChuNhiem);
        
        // Header text
        txtDangNhap = findViewById(R.id.txtDangNhap);
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

        btnPhanCongGiangDay.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PhanCongGiangDayActivity.class);
            startActivity(intent);
        });

        btnPhanCongChuNhiem.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PhanCongChuNhiemActivity.class);
            startActivity(intent);
        });

        // CardView listeners
        cardLopHoc.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LopListActivity.class);
            startActivity(intent);
        });

        cardSinhVien.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        cardNganh.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NganhListActivity.class);
            startActivity(intent);
        });

        cardMonHoc.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MonHocListActivity.class);
            startActivity(intent);
        });

        cardGiaoVien.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GiaoVienListActivity.class);
            startActivity(intent);
        });

        cardNguoiDung.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UsersListActivity.class);
            startActivity(intent);
        });

        cardKhoa.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, KhoaListActivity.class);
            startActivity(intent);
        });

        cardPhanCongGiangDay.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PhanCongGiangDayActivity.class);
            startActivity(intent);
        });

        cardPhanCongChuNhiem.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PhanCongChuNhiemActivity.class);
            startActivity(intent);
        });

        // Login text click
        txtDangNhap.setOnClickListener(v -> {
            String userName = sharedPreferences.getString("userName", null);
            if (userName == null || userName.isEmpty()) {
                // Chưa đăng nhập - mở LoginActivity
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                // Đã đăng nhập - có thể mở menu hoặc profile
                // Tạm thời không làm gì
            }
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
            
            // Cập nhật text "Đăng nhập" thành tên người dùng
            String displayName = fullName != null ? fullName : userName;
            txtDangNhap.setText(displayName);
            txtDangNhap.setClickable(true);
            
            String userInfo = "Xin chào: " + displayName;
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
            
            // Hiển thị "Đăng nhập"
            txtDangNhap.setText("Đăng nhập");
            txtDangNhap.setClickable(true);
        }
    }
}

