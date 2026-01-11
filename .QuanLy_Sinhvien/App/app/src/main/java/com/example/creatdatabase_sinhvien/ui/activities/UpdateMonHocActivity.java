package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.MonHoc;
import com.example.creatdatabase_sinhvien.repositories.MonHocRepository;

/**
 * Activity để cập nhật thông tin môn học
 */
public class UpdateMonHocActivity extends AppCompatActivity {
    private EditText edtMaMH, edtTenMon, edtSoTinChi;
    private Button btnHuy, btnLuu, btnXoa;
    
    private MonHocRepository monHocRepository;
    private MonHoc currentMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_monhoc);

        // Lấy dữ liệu môn học từ Intent
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            currentMonHoc = getIntent().getSerializableExtra("MON_HOC", MonHoc.class);
        } else {
            currentMonHoc = (MonHoc) getIntent().getSerializableExtra("MON_HOC");
        }
        
        if (currentMonHoc == null) {
            Toast.makeText(this, "Không có dữ liệu môn học", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        
        monHocRepository = new MonHocRepository();
        loadMonHocData();
    }

    private void initViews() {
        edtMaMH = findViewById(R.id.edtMaMH);
        edtTenMon = findViewById(R.id.edtTenMon);
        edtSoTinChi = findViewById(R.id.edtSoTinChi);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
        btnXoa = findViewById(R.id.btnXoa);
        
        // Mã môn học chỉ xem, không cho sửa
        edtMaMH.setEnabled(false);
        edtMaMH.setFocusable(false);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> updateMonHoc());
        btnXoa.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void loadMonHocData() {
        if (currentMonHoc != null) {
            edtMaMH.setText(currentMonHoc.getMaMH());
            edtTenMon.setText(currentMonHoc.getTenMon());
            edtSoTinChi.setText(String.valueOf(currentMonHoc.getSoTinChi() != null ? currentMonHoc.getSoTinChi() : 0));
        }
    }

    private void updateMonHoc() {
        // Validate
        String tenMon = edtTenMon.getText().toString().trim();
        String soTinChiStr = edtSoTinChi.getText().toString().trim();

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

        // Cập nhật đối tượng MonHoc
        MonHoc monHoc = new MonHoc();
        monHoc.setMaMH(currentMonHoc.getMaMH()); // Giữ nguyên mã môn học
        monHoc.setTenMon(tenMon);
        monHoc.setSoTinChi(soTinChi);

        btnLuu.setEnabled(false);
        Toast.makeText(this, "Đang cập nhật...", Toast.LENGTH_SHORT).show();

        monHocRepository.updateMonHoc(currentMonHoc.getMaMH(), monHoc, new MonHocRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateMonHocActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateMonHocActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    btnLuu.setEnabled(true);
                });
            }
        });
    }

    private void showDeleteConfirmation() {
        String monHocName = currentMonHoc.getTenMon() != null ? currentMonHoc.getTenMon() : currentMonHoc.getMaMH();
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa môn " + monHocName + "?")
            .setPositiveButton("Đồng ý", (dialog, which) -> deleteMonHoc())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteMonHoc() {
        monHocRepository.deleteMonHoc(currentMonHoc.getMaMH(), new MonHocRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateMonHocActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateMonHocActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

