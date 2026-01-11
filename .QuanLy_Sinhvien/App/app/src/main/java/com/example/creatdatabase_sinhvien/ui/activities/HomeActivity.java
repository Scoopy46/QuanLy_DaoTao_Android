package com.example.creatdatabase_sinhvien.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.repositories.GiaoVienRepository;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import com.example.creatdatabase_sinhvien.repositories.LopRepository;
import com.example.creatdatabase_sinhvien.repositories.MonHocRepository;
import com.example.creatdatabase_sinhvien.repositories.NganhRepository;
import com.example.creatdatabase_sinhvien.repositories.SinhVienRepository;
import com.example.creatdatabase_sinhvien.repositories.UsersRepository;

/**
 * Màn hình Home - Quản lý đào tạo
 */
public class HomeActivity extends AppCompatActivity {
    private android.view.View btnQuanLySinhVien, btnQuanLyGiaoVien, btnQuanLyLop, btnQuanLyNguoiDung, btnQuanLyNganh, btnQuanLyMonHoc, btnQuanLyKhoa, btnPhanCongGiangDay, btnPhanCongChuNhiem;
    private ImageButton btnLogin, btnLogout;
    private TextView txtUserInfo;

    private TextView tvCountSinhVien, tvCountLop, tvCountMonHoc, tvCountGiaoVien, tvCountKhoa, tvCountNganh;

    private SharedPreferences sharedPreferences;

    private SinhVienRepository sinhVienRepository;
    private LopRepository lopRepository;
    private MonHocRepository monHocRepository;
    private GiaoVienRepository giaoVienRepository;
    private KhoaRepository khoaRepository;
    private NganhRepository nganhRepository;
    private UsersRepository usersRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        
        initViews();
        setupListeners();

        sinhVienRepository = new SinhVienRepository();
        lopRepository = new LopRepository();
        monHocRepository = new MonHocRepository();
        giaoVienRepository = new GiaoVienRepository();
        khoaRepository = new KhoaRepository();
        nganhRepository = new NganhRepository();
        usersRepository = new UsersRepository();

        updateLoginUI();
        loadStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật UI khi quay lại màn hình
        updateLoginUI();
        loadStats();
    }

    private void initViews() {
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

        tvCountSinhVien = findViewById(R.id.tvCountSinhVien);
        tvCountLop = findViewById(R.id.tvCountLop);
        tvCountMonHoc = findViewById(R.id.tvCountMonHoc);
        tvCountGiaoVien = findViewById(R.id.tvCountGiaoVien);
        tvCountKhoa = findViewById(R.id.tvCountKhoa);
        tvCountNganh = findViewById(R.id.tvCountNganh);
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

    private void loadStats() {
        // SV
        sinhVienRepository.getAllSinhVien(new SinhVienRepository.SinhVienCallback() {
            @Override
            public void onSuccess(java.util.List<com.example.creatdatabase_sinhvien.models.SinhVien> sinhViens) {
                runOnUiThread(() -> tvCountSinhVien.setText(String.valueOf(sinhViens != null ? sinhViens.size() : 0)));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> tvCountSinhVien.setText("--"));
            }
        });

        // Lớp
        lopRepository.getAllLop(new LopRepository.LopCallback() {
            @Override
            public void onSuccess(java.util.List<com.example.creatdatabase_sinhvien.models.Lop> lops) {
                runOnUiThread(() -> tvCountLop.setText(String.valueOf(lops != null ? lops.size() : 0)));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> tvCountLop.setText("--"));
            }
        });

        // Môn
        monHocRepository.getAllMonHoc(new MonHocRepository.MonHocCallback() {
            @Override
            public void onSuccess(java.util.List<com.example.creatdatabase_sinhvien.models.MonHoc> monHocList) {
                runOnUiThread(() -> tvCountMonHoc.setText(String.valueOf(monHocList != null ? monHocList.size() : 0)));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> tvCountMonHoc.setText("--"));
            }
        });

        // GV
        giaoVienRepository.getAllGiaoVien(null, new GiaoVienRepository.GiaoVienCallback() {
            @Override
            public void onSuccess(java.util.List<com.example.creatdatabase_sinhvien.models.GiaoVien> giaoViens) {
                runOnUiThread(() -> tvCountGiaoVien.setText(String.valueOf(giaoViens != null ? giaoViens.size() : 0)));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> tvCountGiaoVien.setText("--"));
            }
        });

        // Khoa
        khoaRepository.getAllKhoa(new KhoaRepository.KhoaCallback() {
            @Override
            public void onSuccess(java.util.List<com.example.creatdatabase_sinhvien.models.Khoa> khoas) {
                runOnUiThread(() -> tvCountKhoa.setText(String.valueOf(khoas != null ? khoas.size() : 0)));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> tvCountKhoa.setText("--"));
            }
        });

        // Ngành
        nganhRepository.getAllNganh(new NganhRepository.NganhCallback() {
            @Override
            public void onSuccess(java.util.List<com.example.creatdatabase_sinhvien.models.Nganh> nganhs) {
                runOnUiThread(() -> tvCountNganh.setText(String.valueOf(nganhs != null ? nganhs.size() : 0)));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> tvCountNganh.setText("--"));
            }
        });
    }
}

