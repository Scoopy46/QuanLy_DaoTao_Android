package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.GiaoVien;
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.repositories.GiaoVienRepository;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để thêm giáo viên mới
 */
public class AddGiaoVienActivity extends AppCompatActivity {
    private EditText edtMaGV, edtHoTen, edtHocHam, edtHocVi, edtEmail, edtSDT, edtAnh;
    private Spinner spinnerKhoa;
    private Button btnHuy, btnLuu;
    
    private GiaoVienRepository giaoVienRepository;
    private KhoaRepository khoaRepository;
    private List<Khoa> khoaList;
    private String selectedMaKhoa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_giaovien);

        initViews();
        setupListeners();
        
        giaoVienRepository = new GiaoVienRepository();
        khoaRepository = new KhoaRepository();
        khoaList = new ArrayList<>();
        
        loadKhoaList();
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
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        
        btnLuu.setOnClickListener(v -> saveGiaoVien());
        
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
                    Toast.makeText(AddGiaoVienActivity.this, "Lỗi tải danh sách khoa: " + error, Toast.LENGTH_SHORT).show();
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
    }

    private void saveGiaoVien() {
        // Validate
        String maGV = edtMaGV.getText().toString().trim();
        String hoTen = edtHoTen.getText().toString().trim();
        String hocHam = edtHocHam.getText().toString().trim();
        String hocVi = edtHocVi.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSDT.getText().toString().trim();
        String anh = edtAnh.getText().toString().trim();

        if (maGV.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mã GV", Toast.LENGTH_SHORT).show();
            return;
        }
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

        // Tạo đối tượng GiaoVien (không set userID)
        GiaoVien giaoVien = new GiaoVien();
        giaoVien.setMaGV(maGV);
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
        giaoVienRepository.createGiaoVien(giaoVien, new GiaoVienRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(AddGiaoVienActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AddGiaoVienActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

