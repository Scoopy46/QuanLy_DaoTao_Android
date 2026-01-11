package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.models.Nganh;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import com.example.creatdatabase_sinhvien.repositories.NganhRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để cập nhật thông tin ngành
 */
public class UpdateNganhActivity extends AppCompatActivity {
    private EditText edtMaNganh, edtTenNganh;
    private Spinner spinnerKhoa;
    private Button btnHuy, btnLuu, btnXoa;
    
    private NganhRepository nganhRepository;
    private KhoaRepository khoaRepository;
    private List<Khoa> khoaList;
    private String selectedMaKhoa = "";
    private Nganh currentNganh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nganh);

        // Lấy dữ liệu ngành từ Intent
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            currentNganh = getIntent().getSerializableExtra("NGANH", Nganh.class);
        } else {
            currentNganh = (Nganh) getIntent().getSerializableExtra("NGANH");
        }
        
        if (currentNganh == null) {
            Toast.makeText(this, "Không có dữ liệu ngành", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        
        nganhRepository = new NganhRepository();
        khoaRepository = new KhoaRepository();
        khoaList = new ArrayList<>();
        
        loadKhoaList();
        loadNganhData();
    }

    private void initViews() {
        edtMaNganh = findViewById(R.id.edtMaNganh);
        edtTenNganh = findViewById(R.id.edtTenNganh);
        spinnerKhoa = findViewById(R.id.spinnerKhoa);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
        btnXoa = findViewById(R.id.btnXoa);
        
        // Mã ngành chỉ xem, không cho sửa
        edtMaNganh.setEnabled(false);
        edtMaNganh.setFocusable(false);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> updateNganh());
        btnXoa.setOnClickListener(v -> showDeleteConfirmation());
        
        spinnerKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && khoaList != null && position <= khoaList.size()) {
                    Khoa selectedKhoa = khoaList.get(position - 1);
                    selectedMaKhoa = selectedKhoa.getMaKhoa();
                } else {
                    selectedMaKhoa = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMaKhoa = "";
            }
        });
    }

    private void loadKhoaList() {
        khoaRepository.getAllKhoa(new KhoaRepository.KhoaCallback() {
            @Override
            public void onSuccess(List<Khoa> khoas) {
                runOnUiThread(() -> {
                    khoaList = khoas;
                    setupKhoaSpinner();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateNganhActivity.this, "Lỗi tải danh sách khoa: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void setupKhoaSpinner() {
        List<String> khoaNames = new ArrayList<>();
        khoaNames.add("-- Chọn Khoa --");
        
        if (khoaList != null) {
            for (Khoa khoa : khoaList) {
                khoaNames.add(khoa.getTenKhoa());
            }
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, khoaNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhoa.setAdapter(adapter);
        
        // Chọn khoa hiện tại của ngành
        if (currentNganh != null && currentNganh.getMaKhoa() != null) {
            for (int i = 0; i < khoaList.size(); i++) {
                if (khoaList.get(i).getMaKhoa().equals(currentNganh.getMaKhoa())) {
                    spinnerKhoa.setSelection(i + 1);
                    selectedMaKhoa = currentNganh.getMaKhoa();
                    break;
                }
            }
        }
    }

    private void loadNganhData() {
        if (currentNganh != null) {
            edtMaNganh.setText(currentNganh.getMaNganh());
            edtTenNganh.setText(currentNganh.getTenNganh());
            selectedMaKhoa = currentNganh.getMaKhoa();
        }
    }

    private void updateNganh() {
        // Validate
        String tenNganh = edtTenNganh.getText().toString().trim();

        if (tenNganh.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên ngành", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedMaKhoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn Khoa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật đối tượng Nganh
        Nganh nganh = new Nganh();
        nganh.setMaNganh(currentNganh.getMaNganh()); // Giữ nguyên mã ngành
        nganh.setTenNganh(tenNganh);
        nganh.setMaKhoa(selectedMaKhoa);

        btnLuu.setEnabled(false);
        Toast.makeText(this, "Đang cập nhật...", Toast.LENGTH_SHORT).show();

        nganhRepository.updateNganh(currentNganh.getMaNganh(), nganh, new NganhRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateNganhActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateNganhActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    btnLuu.setEnabled(true);
                });
            }
        });
    }

    private void showDeleteConfirmation() {
        String nganhName = currentNganh.getTenNganh() != null ? currentNganh.getTenNganh() : currentNganh.getMaNganh();
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa ngành " + nganhName + " không?")
            .setPositiveButton("Đồng ý", (dialog, which) -> deleteNganh())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteNganh() {
        nganhRepository.deleteNganh(currentNganh.getMaNganh(), new NganhRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateNganhActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateNganhActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

