package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;

/**
 * Activity để cập nhật thông tin khoa
 */
public class UpdateKhoaActivity extends AppCompatActivity {
    private EditText edtMaKhoa, edtTenKhoa;
    private Button btnHuy, btnLuu, btnXoa;
    
    private KhoaRepository khoaRepository;
    private Khoa currentKhoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_khoa);

        // Lấy dữ liệu khoa từ Intent
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            currentKhoa = getIntent().getSerializableExtra("KHOA", Khoa.class);
        } else {
            currentKhoa = (Khoa) getIntent().getSerializableExtra("KHOA");
        }
        
        if (currentKhoa == null) {
            Toast.makeText(this, "Không có dữ liệu khoa", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        
        khoaRepository = new KhoaRepository();
        loadKhoaData();
    }

    private void initViews() {
        edtMaKhoa = findViewById(R.id.edtMaKhoa);
        edtTenKhoa = findViewById(R.id.edtTenKhoa);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
        btnXoa = findViewById(R.id.btnXoa);
        
        // Mã khoa chỉ xem, không cho sửa
        edtMaKhoa.setEnabled(false);
        edtMaKhoa.setFocusable(false);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> updateKhoa());
        btnXoa.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void loadKhoaData() {
        if (currentKhoa != null) {
            edtMaKhoa.setText(currentKhoa.getMaKhoa());
            edtTenKhoa.setText(currentKhoa.getTenKhoa());
        }
    }

    private void updateKhoa() {
        // Validate
        String tenKhoa = edtTenKhoa.getText().toString().trim();

        if (tenKhoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên khoa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật đối tượng Khoa
        Khoa khoa = new Khoa();
        khoa.setMaKhoa(currentKhoa.getMaKhoa()); // Giữ nguyên mã khoa
        khoa.setTenKhoa(tenKhoa);

        btnLuu.setEnabled(false);
        Toast.makeText(this, "Đang cập nhật...", Toast.LENGTH_SHORT).show();

        khoaRepository.updateKhoa(currentKhoa.getMaKhoa(), khoa, new KhoaRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateKhoaActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateKhoaActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    btnLuu.setEnabled(true);
                });
            }
        });
    }

    private void showDeleteConfirmation() {
        String khoaName = currentKhoa.getTenKhoa() != null ? currentKhoa.getTenKhoa() : currentKhoa.getMaKhoa();
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa khoa " + khoaName + "?")
            .setPositiveButton("Đồng ý", (dialog, which) -> deleteKhoa())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteKhoa() {
        khoaRepository.deleteKhoa(currentKhoa.getMaKhoa(), new KhoaRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateKhoaActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateKhoaActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

