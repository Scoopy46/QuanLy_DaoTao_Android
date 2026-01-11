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
import com.example.creatdatabase_sinhvien.models.Lop;
import com.example.creatdatabase_sinhvien.models.Nganh;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import com.example.creatdatabase_sinhvien.repositories.LopRepository;
import com.example.creatdatabase_sinhvien.repositories.NganhRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để thêm lớp mới
 */
public class AddLopActivity extends AppCompatActivity {
    private EditText edtMaLop, edtTenLop, edtNienKhoa;
    private Spinner spinnerNganh;
    private Button btnHuy, btnLuu;
    
    private LopRepository lopRepository;
    private NganhRepository nganhRepository;
    private KhoaRepository khoaRepository;
    private List<Nganh> nganhList;
    private List<Khoa> khoaList;
    private String selectedMaNganh = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lop);

        initViews();
        setupListeners();
        
        lopRepository = new LopRepository();
        nganhRepository = new NganhRepository();
        khoaRepository = new KhoaRepository();
        nganhList = new ArrayList<>();
        khoaList = new ArrayList<>();
        
        loadKhoaList();
        loadNganhList();
    }

    private void initViews() {
        edtMaLop = findViewById(R.id.edtMaLop);
        edtTenLop = findViewById(R.id.edtTenLop);
        edtNienKhoa = findViewById(R.id.edtNienKhoa);
        spinnerNganh = findViewById(R.id.spinnerNganh);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        
        btnLuu.setOnClickListener(v -> saveLop());
        
        spinnerNganh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && nganhList != null && position <= nganhList.size()) {
                    Nganh selectedNganh = nganhList.get(position - 1);
                    selectedMaNganh = selectedNganh.getMaNganh();
                } else {
                    selectedMaNganh = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMaNganh = "";
            }
        });
    }

    private void loadKhoaList() {
        khoaRepository.getAllKhoa(new KhoaRepository.KhoaCallback() {
            @Override
            public void onSuccess(List<Khoa> khoas) {
                runOnUiThread(() -> {
                    khoaList = khoas;
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    // Không hiển thị lỗi, chỉ log
                    android.util.Log.e("AddLopActivity", "Lỗi tải danh sách khoa: " + error);
                });
            }
        });
    }

    private void loadNganhList() {
        nganhRepository.getAllNganh(new NganhRepository.NganhCallback() {
            @Override
            public void onSuccess(List<Nganh> nganhs) {
                runOnUiThread(() -> {
                    nganhList = nganhs;
                    setupNganhSpinner();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AddLopActivity.this, "Lỗi tải danh sách ngành: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void setupNganhSpinner() {
        List<String> nganhNames = new ArrayList<>();
        nganhNames.add("-- Chọn Ngành --");
        
        if (nganhList != null) {
            for (Nganh nganh : nganhList) {
                nganhNames.add(nganh.getTenNganh());
            }
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, nganhNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNganh.setAdapter(adapter);
    }

    private void saveLop() {
        // Validate
        String maLop = edtMaLop.getText().toString().trim();
        String tenLop = edtTenLop.getText().toString().trim();
        String nienKhoa = edtNienKhoa.getText().toString().trim();

        if (maLop.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mã lớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedMaNganh.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn Ngành", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tìm tên ngành và tenKhoa
        String tenNganh = "";
        String maKhoa = "";
        for (Nganh nganh : nganhList) {
            if (nganh.getMaNganh().equals(selectedMaNganh)) {
                tenNganh = nganh.getTenNganh();
                maKhoa = nganh.getMaKhoa();
                break;
            }
        }

        // Tìm tenKhoa từ maKhoa
        String tenKhoa = "";
        if (!maKhoa.isEmpty() && khoaList != null) {
            for (Khoa khoa : khoaList) {
                if (khoa.getMaKhoa().equals(maKhoa)) {
                    tenKhoa = khoa.getTenKhoa();
                    break;
                }
            }
        }

        // Tạo đối tượng Lop
        Lop lop = new Lop();
        lop.setMaLop(maLop);
        lop.setTenLop(tenLop);
        lop.setNienKhoa(nienKhoa);
        lop.setMaNganh(selectedMaNganh);
        lop.setTenNganh(tenNganh); // Chỉ để hiển thị
        lop.setTenKhoa(tenKhoa); // Lấy từ Khoa thông qua Nganh

        // Gửi request
        lopRepository.createLop(lop, new LopRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(AddLopActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AddLopActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

