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
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.models.Nganh;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import com.example.creatdatabase_sinhvien.repositories.NganhRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để thêm ngành mới
 */
public class AddNganhActivity extends AppCompatActivity {
    private EditText edtMaNganh, edtTenNganh;
    private Spinner spinnerKhoa;
    private Button btnHuy, btnLuu;
    
    private NganhRepository nganhRepository;
    private KhoaRepository khoaRepository;
    private List<Khoa> khoaList;
    private String selectedMaKhoa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nganh);

        initViews();
        setupListeners();
        
        nganhRepository = new NganhRepository();
        khoaRepository = new KhoaRepository();
        khoaList = new ArrayList<>();
        
        loadKhoaList();
    }

    private void initViews() {
        edtMaNganh = findViewById(R.id.edtMaNganh);
        edtTenNganh = findViewById(R.id.edtTenNganh);
        spinnerKhoa = findViewById(R.id.spinnerKhoa);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> saveNganh());
        
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
                    Toast.makeText(AddNganhActivity.this, "Lỗi tải danh sách khoa: " + error, Toast.LENGTH_SHORT).show();
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

    private void saveNganh() {
        // Validate
        String maNganh = edtMaNganh.getText().toString().trim();
        String tenNganh = edtTenNganh.getText().toString().trim();

        if (maNganh.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mã ngành", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tenNganh.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên ngành", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedMaKhoa.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn Khoa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Nganh
        Nganh nganh = new Nganh();
        nganh.setMaNganh(maNganh);
        nganh.setTenNganh(tenNganh);
        nganh.setMaKhoa(selectedMaKhoa);

        // Gửi request
        btnLuu.setEnabled(false);
        Toast.makeText(this, "Đang lưu...", Toast.LENGTH_SHORT).show();

        nganhRepository.createNganh(nganh, new NganhRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(AddNganhActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AddNganhActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    btnLuu.setEnabled(true);
                });
            }
        });
    }
}

