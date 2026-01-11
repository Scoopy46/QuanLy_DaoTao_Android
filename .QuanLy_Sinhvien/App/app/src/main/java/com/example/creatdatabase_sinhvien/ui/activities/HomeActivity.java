package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;

/**
 * Màn hình Home - Quản lý đào tạo
 */
public class HomeActivity extends AppCompatActivity {
    private Button btnQuanLySinhVien, btnQuanLyGiaoVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnQuanLySinhVien = findViewById(R.id.btnQuanLySinhVien);
        btnQuanLyGiaoVien = findViewById(R.id.btnQuanLyGiaoVien);
    }

    private void setupListeners() {
        btnQuanLySinhVien.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnQuanLyGiaoVien.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(HomeActivity.this, GiaoVienListActivity.class);
            startActivity(intent);
        });
    }
}

