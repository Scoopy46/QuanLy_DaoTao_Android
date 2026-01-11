package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;

/**
 * Activity để thêm khoa mới
 */
public class AddKhoaActivity extends AppCompatActivity {
    private EditText edtMaKhoa, edtTenKhoa;
    private Button btnHuy, btnLuu;
    
    private KhoaRepository khoaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_khoa);

        initViews();
        setupListeners();
        
        khoaRepository = new KhoaRepository();
    }

    private void initViews() {
        edtMaKhoa = findViewById(R.id.edtMaKhoa);
        edtTenKhoa = findViewById(R.id.edtTenKhoa);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> saveKhoa());
    }

    private void saveKhoa() {
        // Validate
        String maKhoa = edtMaKhoa.getText().toString().trim();
        String tenKhoa = edtTenKhoa.getText().toString().trim();

        if (maKhoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mã khoa", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tenKhoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên khoa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Khoa
        Khoa khoa = new Khoa();
        khoa.setMaKhoa(maKhoa);
        khoa.setTenKhoa(tenKhoa);

        // Gửi request
        btnLuu.setEnabled(false);
        Toast.makeText(this, "Đang lưu...", Toast.LENGTH_SHORT).show();

        khoaRepository.createKhoa(khoa, new KhoaRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(AddKhoaActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AddKhoaActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    btnLuu.setEnabled(true);
                });
            }
        });
    }
}

