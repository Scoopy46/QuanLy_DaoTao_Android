package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.MonHoc;
import com.example.creatdatabase_sinhvien.repositories.MonHocRepository;

/**
 * Activity để thêm môn học mới
 */
public class AddMonHocActivity extends AppCompatActivity {
    private EditText edtMaMH, edtTenMon, edtSoTinChi;
    private Button btnHuy, btnLuu;
    
    private MonHocRepository monHocRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monhoc);

        initViews();
        setupListeners();
        
        monHocRepository = new MonHocRepository();
    }

    private void initViews() {
        edtMaMH = findViewById(R.id.edtMaMH);
        edtTenMon = findViewById(R.id.edtTenMon);
        edtSoTinChi = findViewById(R.id.edtSoTinChi);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> saveMonHoc());
    }

    private void saveMonHoc() {
        // Validate
        String maMH = edtMaMH.getText().toString().trim();
        String tenMon = edtTenMon.getText().toString().trim();
        String soTinChiStr = edtSoTinChi.getText().toString().trim();

        if (maMH.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mã môn học", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tenMon.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên môn học", Toast.LENGTH_SHORT).show();
            return;
        }
        if (soTinChiStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Số tín chỉ", Toast.LENGTH_SHORT).show();
            return;
        }

        int soTinChi;
        try {
            soTinChi = Integer.parseInt(soTinChiStr);
            if (soTinChi <= 0) {
                Toast.makeText(this, "Số tín chỉ phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tín chỉ không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng MonHoc
        MonHoc monHoc = new MonHoc();
        monHoc.setMaMH(maMH);
        monHoc.setTenMon(tenMon);
        monHoc.setSoTinChi(soTinChi);

        // Gửi request
        btnLuu.setEnabled(false);
        Toast.makeText(this, "Đang lưu...", Toast.LENGTH_SHORT).show();

        monHocRepository.createMonHoc(monHoc, new MonHocRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(AddMonHocActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AddMonHocActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    btnLuu.setEnabled(true);
                });
            }
        });
    }
}

