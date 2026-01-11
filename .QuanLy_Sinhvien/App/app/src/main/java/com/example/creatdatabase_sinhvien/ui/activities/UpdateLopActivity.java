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
import com.example.creatdatabase_sinhvien.models.Lop;
import com.example.creatdatabase_sinhvien.models.Nganh;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import com.example.creatdatabase_sinhvien.repositories.LopRepository;
import com.example.creatdatabase_sinhvien.repositories.NganhRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để cập nhật thông tin lớp
 */
public class UpdateLopActivity extends AppCompatActivity {
    private EditText edtMaLop, edtTenLop, edtNienKhoa;
    private Spinner spinnerNganh;
    private Button btnHuy, btnLuu, btnXoa;
    
    private LopRepository lopRepository;
    private NganhRepository nganhRepository;
    private KhoaRepository khoaRepository;
    private List<Nganh> nganhList;
    private List<Khoa> khoaList;
    private String selectedMaNganh = "";
    private Lop currentLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lop);

        // Lấy dữ liệu lớp từ Intent
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            currentLop = getIntent().getSerializableExtra("LOP", Lop.class);
        } else {
            currentLop = (Lop) getIntent().getSerializableExtra("LOP");
        }
        
        if (currentLop == null) {
            Toast.makeText(this, "Không có dữ liệu lớp", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        
        lopRepository = new LopRepository();
        nganhRepository = new NganhRepository();
        khoaRepository = new KhoaRepository();
        nganhList = new ArrayList<>();
        khoaList = new ArrayList<>();
        
        loadKhoaList();
        loadNganhList();
        loadLopData();
    }

    private void initViews() {
        edtMaLop = findViewById(R.id.edtMaLop);
        edtTenLop = findViewById(R.id.edtTenLop);
        edtNienKhoa = findViewById(R.id.edtNienKhoa);
        spinnerNganh = findViewById(R.id.spinnerNganh);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
        btnXoa = findViewById(R.id.btnXoa);
        
        // Mã lớp chỉ xem, không cho sửa
        edtMaLop.setEnabled(false);
        edtMaLop.setFocusable(false);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        
        btnLuu.setOnClickListener(v -> updateLop());
        
        btnXoa.setOnClickListener(v -> showDeleteConfirmation());
        
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
                    android.util.Log.e("UpdateLopActivity", "Lỗi tải danh sách khoa: " + error);
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
                    Toast.makeText(UpdateLopActivity.this, "Lỗi tải danh sách ngành: " + error, Toast.LENGTH_SHORT).show();
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
        
        // Chọn ngành hiện tại của lớp
        if (currentLop != null && currentLop.getMaNganh() != null) {
            for (int i = 0; i < nganhList.size(); i++) {
                if (nganhList.get(i).getMaNganh().equals(currentLop.getMaNganh())) {
                    spinnerNganh.setSelection(i + 1);
                    selectedMaNganh = currentLop.getMaNganh();
                    break;
                }
            }
        }
    }

    private void loadLopData() {
        if (currentLop != null) {
            edtMaLop.setText(currentLop.getMaLop());
            edtTenLop.setText(currentLop.getTenLop());
            edtNienKhoa.setText(currentLop.getNienKhoa());
            selectedMaNganh = currentLop.getMaNganh();
        }
    }

    private void updateLop() {
        // Validate
        String tenLop = edtTenLop.getText().toString().trim();
        String nienKhoa = edtNienKhoa.getText().toString().trim();

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
        
        // Nếu không tìm thấy, dùng tenKhoa từ currentLop
        if (tenKhoa.isEmpty() && currentLop.getTenKhoa() != null) {
            tenKhoa = currentLop.getTenKhoa();
        }

        // Cập nhật đối tượng Lop
        Lop lop = new Lop();
        lop.setMaLop(currentLop.getMaLop()); // Giữ nguyên mã lớp
        lop.setTenLop(tenLop);
        lop.setNienKhoa(nienKhoa);
        lop.setMaNganh(selectedMaNganh);
        lop.setTenNganh(tenNganh); // Chỉ để hiển thị
        lop.setTenKhoa(tenKhoa); // Lấy từ Khoa thông qua Nganh

        // Gửi request
        lopRepository.updateLop(currentLop.getMaLop(), lop, new LopRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateLopActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateLopActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void showDeleteConfirmation() {
        String lopName = currentLop.getTenLop() != null ? currentLop.getTenLop() : currentLop.getMaLop();
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa lớp " + lopName + " không?")
            .setPositiveButton("Đồng ý", (dialog, which) -> deleteLop())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteLop() {
        lopRepository.deleteLop(currentLop.getMaLop(), new LopRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateLopActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateLopActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

