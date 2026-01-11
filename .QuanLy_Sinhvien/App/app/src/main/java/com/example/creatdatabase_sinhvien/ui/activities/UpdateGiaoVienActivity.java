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
import com.example.creatdatabase_sinhvien.models.GiaoVien;
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.repositories.GiaoVienRepository;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để cập nhật thông tin giáo viên
 */
public class UpdateGiaoVienActivity extends AppCompatActivity {
    private EditText edtMaGV, edtHoTen, edtHocHam, edtHocVi, edtEmail, edtSDT, edtAnh;
    private Spinner spinnerKhoa;
    private Button btnHuy, btnLuu, btnXoa;
    
    private GiaoVienRepository giaoVienRepository;
    private KhoaRepository khoaRepository;
    private List<Khoa> khoaList;
    private String selectedMaKhoa = "";
    private GiaoVien currentGiaoVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_giaovien);

        // Lấy dữ liệu giáo viên từ Intent
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            currentGiaoVien = getIntent().getSerializableExtra("GIAO_VIEN", GiaoVien.class);
        } else {
            currentGiaoVien = (GiaoVien) getIntent().getSerializableExtra("GIAO_VIEN");
        }
        if (currentGiaoVien == null) {
            Toast.makeText(this, "Không có dữ liệu giáo viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        
        giaoVienRepository = new GiaoVienRepository();
        khoaRepository = new KhoaRepository();
        khoaList = new ArrayList<>();
        
        loadKhoaList();
        loadGiaoVienData();
    }

    private void initViews() {
        edtMaGV = findViewById(R.id.edtMaGV);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtHocHam = findViewById(R.id.edtHocHam);
        edtHocVi = findViewById(R.id.edtHocVi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtAnh = findViewById(R.id.edtAnh);
        spinnerKhoa = findViewById(R.id.spinnerKhoa);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
        btnXoa = findViewById(R.id.btnXoa);
        
        // Mã GV chỉ xem, không cho sửa
        edtMaGV.setEnabled(false);
        edtMaGV.setFocusable(false);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        
        btnLuu.setOnClickListener(v -> updateGiaoVien());
        
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
                    Toast.makeText(UpdateGiaoVienActivity.this, "Lỗi tải danh sách khoa: " + error, Toast.LENGTH_SHORT).show();
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
        
        // Chọn khoa hiện tại của giáo viên
        if (currentGiaoVien != null && currentGiaoVien.getMaKhoa() != null) {
            for (int i = 0; i < khoaList.size(); i++) {
                if (khoaList.get(i).getMaKhoa().equals(currentGiaoVien.getMaKhoa())) {
                    spinnerKhoa.setSelection(i + 1);
                    selectedMaKhoa = currentGiaoVien.getMaKhoa();
                    break;
                }
            }
        }
    }

    private void loadGiaoVienData() {
        if (currentGiaoVien != null) {
            edtMaGV.setText(currentGiaoVien.getMaGV());
            edtHoTen.setText(currentGiaoVien.getHoten());
            edtHocHam.setText(currentGiaoVien.getHocHam());
            edtHocVi.setText(currentGiaoVien.getHocVi());
            edtEmail.setText(currentGiaoVien.getEmail());
            edtSDT.setText(currentGiaoVien.getSdt());
            edtAnh.setText(currentGiaoVien.getAnh());
            selectedMaKhoa = currentGiaoVien.getMaKhoa();
        }
    }

    private void updateGiaoVien() {
        // Validate
        String hoTen = edtHoTen.getText().toString().trim();
        String hocHam = edtHocHam.getText().toString().trim();
        String hocVi = edtHocVi.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSDT.getText().toString().trim();
        String anh = edtAnh.getText().toString().trim();

        if (hoTen.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Họ tên", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedMaKhoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn Khoa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tìm tên khoa
        String tenKhoa = "";
        for (Khoa khoa : khoaList) {
            if (khoa.getMaKhoa().equals(selectedMaKhoa)) {
                tenKhoa = khoa.getTenKhoa();
                break;
            }
        }

        // Cập nhật đối tượng GiaoVien (không set userID)
        GiaoVien giaoVien = new GiaoVien();
        giaoVien.setMaGV(currentGiaoVien.getMaGV()); // Giữ nguyên mã GV
        giaoVien.setHoten(hoTen);
        giaoVien.setHocHam(hocHam);
        giaoVien.setHocVi(hocVi);
        giaoVien.setEmail(email);
        giaoVien.setSdt(sdt);
        giaoVien.setAnh(anh);
        giaoVien.setMaKhoa(selectedMaKhoa);
        giaoVien.setTenKhoa(tenKhoa);
        giaoVien.setUserID(null); // Bỏ qua userID

        // Gửi request
        giaoVienRepository.updateGiaoVien(currentGiaoVien.getMaGV(), giaoVien, 
            new GiaoVienRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(UpdateGiaoVienActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(UpdateGiaoVienActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                    });
                }
            });
    }

    private void showDeleteConfirmation() {
        String teacherName = currentGiaoVien.getHoten() != null ? currentGiaoVien.getHoten() : "";
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa giáo viên " + teacherName + " không?")
            .setPositiveButton("Đồng ý", (dialog, which) -> deleteGiaoVien())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteGiaoVien() {
        giaoVienRepository.deleteGiaoVien(currentGiaoVien.getMaGV(), 
            new GiaoVienRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(UpdateGiaoVienActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(UpdateGiaoVienActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                    });
                }
            });
    }
}

