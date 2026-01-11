package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.adapters.DiemThiAdapter;
import com.example.creatdatabase_sinhvien.models.DiemThi;
import com.example.creatdatabase_sinhvien.models.MonHoc;
import com.example.creatdatabase_sinhvien.models.SinhVien;
import com.example.creatdatabase_sinhvien.repositories.DiemThiRepository;
import com.example.creatdatabase_sinhvien.repositories.MonHocRepository;
import com.example.creatdatabase_sinhvien.repositories.SinhVienRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để nhập điểm thi cho sinh viên
 */
public class NhapDiemActivity extends AppCompatActivity {
    private EditText edtFilterLop, edtFilterMaMH;
    private EditText edtMaSV, edtMaMH, edtDiemLan1, edtDiemLan2;
    private Button btnTimKiem, btnLuuDiem, btnHuy, btnChonSinhVien, btnChonMonHoc;
    private ListView listViewDiemThi;
    
    private DiemThiRepository repository;
    private SinhVienRepository sinhVienRepository;
    private MonHocRepository monHocRepository;
    private DiemThiAdapter adapter;
    private List<DiemThi> diemThiList;
    private List<SinhVien> sinhVienList;
    private List<MonHoc> monHocList;
    private DiemThi selectedDiemThi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_diem);

        initViews();
        setupListeners();
        
        repository = new DiemThiRepository();
        sinhVienRepository = new SinhVienRepository();
        monHocRepository = new MonHocRepository();
        diemThiList = new ArrayList<>();
        sinhVienList = new ArrayList<>();
        monHocList = new ArrayList<>();
        adapter = new DiemThiAdapter(this, diemThiList);
        listViewDiemThi.setAdapter(adapter);

        loadDiemThiList("", "");
        loadSinhVienList();
        loadMonHocList();
    }
    
    private void initViews() {
        edtFilterLop = findViewById(R.id.edtFilterLop);
        edtFilterMaMH = findViewById(R.id.edtFilterMaMH);
        edtMaSV = findViewById(R.id.edtMaSV);
        edtMaMH = findViewById(R.id.edtMaMH);
        edtDiemLan1 = findViewById(R.id.edtDiemLan1);
        edtDiemLan2 = findViewById(R.id.edtDiemLan2);
        btnTimKiem = findViewById(R.id.btnTimKiem);
        btnLuuDiem = findViewById(R.id.btnLuuDiem);
        btnHuy = findViewById(R.id.btnHuy);
        btnChonSinhVien = findViewById(R.id.btnChonSinhVien);
        btnChonMonHoc = findViewById(R.id.btnChonMonHoc);
        listViewDiemThi = findViewById(R.id.listViewDiemThi);
    }

    private void setupListeners() {
        btnTimKiem.setOnClickListener(v -> {
            String lop = edtFilterLop.getText().toString().trim();
            String maMH = edtFilterMaMH.getText().toString().trim();
            loadDiemThiList(lop, maMH);
        });

        btnLuuDiem.setOnClickListener(v -> handleLuuDiem());
        btnHuy.setOnClickListener(v -> clearForm());
        btnChonSinhVien.setOnClickListener(v -> showSinhVienDialog());
        btnChonMonHoc.setOnClickListener(v -> showMonHocDialog());

        listViewDiemThi.setOnItemClickListener((parent, view, position, id) -> {
            selectedDiemThi = diemThiList.get(position);
            fillForm(selectedDiemThi);
        });
    }

    private void loadDiemThiList(String lop, String maMH) {
        Toast.makeText(this, "Đang tải danh sách điểm thi...", Toast.LENGTH_SHORT).show();
        repository.getDiemThi(
            lop.isEmpty() ? null : lop,
            maMH.isEmpty() ? null : maMH,
            new DiemThiRepository.DiemThiCallback() {
                @Override
                public void onSuccess(List<DiemThi> diemThiList) {
                    runOnUiThread(() -> {
                        NhapDiemActivity.this.diemThiList = diemThiList != null ? diemThiList : new ArrayList<>();
                        android.util.Log.d("NhapDiemActivity", "Loaded " + NhapDiemActivity.this.diemThiList.size() + " điểm thi");
                        if (NhapDiemActivity.this.diemThiList.isEmpty()) {
                            Toast.makeText(NhapDiemActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NhapDiemActivity.this, "Đã tải " + NhapDiemActivity.this.diemThiList.size() + " điểm thi", Toast.LENGTH_SHORT).show();
                        }
                        adapter.updateList(NhapDiemActivity.this.diemThiList);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(NhapDiemActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                        android.util.Log.e("NhapDiemActivity", "Error loading: " + error);
                        diemThiList = new ArrayList<>();
                        adapter.updateList(diemThiList);
                    });
                }
            }
        );
    }

    private void handleLuuDiem() {
        if (!validateInput()) {
            return;
        }

        String maSV = edtMaSV.getText().toString().trim();
        String maMH = edtMaMH.getText().toString().trim();
        String diemLan1Str = edtDiemLan1.getText().toString().trim();
        String diemLan2Str = edtDiemLan2.getText().toString().trim();

        Double diemLan1 = diemLan1Str.isEmpty() ? null : Double.parseDouble(diemLan1Str);
        Double diemLan2 = diemLan2Str.isEmpty() ? null : Double.parseDouble(diemLan2Str);

        DiemThi diemThi = new DiemThi(maSV, maMH, diemLan1, diemLan2);

        repository.createOrUpdateDiemThi(diemThi, new DiemThiRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(NhapDiemActivity.this, "Lưu điểm thành công", Toast.LENGTH_SHORT).show();
                clearForm();
                String lop = edtFilterLop.getText().toString().trim();
                String maMH = edtFilterMaMH.getText().toString().trim();
                loadDiemThiList(lop, maMH);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(NhapDiemActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(edtMaSV.getText().toString().trim())) {
            Toast.makeText(this, "Vui lòng chọn sinh viên", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtMaMH.getText().toString().trim())) {
            Toast.makeText(this, "Vui lòng chọn môn học", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        String diemLan1Str = edtDiemLan1.getText().toString().trim();
        String diemLan2Str = edtDiemLan2.getText().toString().trim();
        
        if (diemLan1Str.isEmpty() && diemLan2Str.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập ít nhất một điểm", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!diemLan1Str.isEmpty()) {
            try {
                double diem = Double.parseDouble(diemLan1Str);
                if (diem < 0 || diem > 10) {
                    Toast.makeText(this, "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Điểm lần 1 không hợp lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (!diemLan2Str.isEmpty()) {
            try {
                double diem = Double.parseDouble(diemLan2Str);
                if (diem < 0 || diem > 10) {
                    Toast.makeText(this, "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Điểm lần 2 không hợp lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void fillForm(DiemThi dt) {
        if (dt != null) {
            edtMaSV.setText(dt.getMaSV() != null ? dt.getMaSV() : "");
            edtMaMH.setText(dt.getMaMH() != null ? dt.getMaMH() : "");
            edtDiemLan1.setText(dt.getDiemLan1() != null ? String.valueOf(dt.getDiemLan1()) : "");
            edtDiemLan2.setText(dt.getDiemLan2() != null ? String.valueOf(dt.getDiemLan2()) : "");
        }
    }

    private void clearForm() {
        edtMaSV.setText("");
        edtMaMH.setText("");
        edtDiemLan1.setText("");
        edtDiemLan2.setText("");
        selectedDiemThi = null;
    }

    private void loadSinhVienList() {
        sinhVienRepository.getAllSinhVien(new SinhVienRepository.SinhVienCallback() {
            @Override
            public void onSuccess(List<SinhVien> sinhViens) {
                runOnUiThread(() -> {
                    sinhVienList = sinhViens != null ? sinhViens : new ArrayList<>();
                });
            }

            @Override
            public void onError(String error) {
                android.util.Log.e("NhapDiemActivity", "Error loading sinh vien: " + error);
            }
        });
    }

    private void loadMonHocList() {
        monHocRepository.getAllMonHoc(new MonHocRepository.MonHocCallback() {
            @Override
            public void onSuccess(List<MonHoc> monHocs) {
                runOnUiThread(() -> {
                    monHocList = monHocs != null ? monHocs : new ArrayList<>();
                });
            }

            @Override
            public void onError(String error) {
                android.util.Log.e("NhapDiemActivity", "Error loading mon hoc: " + error);
            }
        });
    }

    private void showSinhVienDialog() {
        if (sinhVienList.isEmpty()) {
            Toast.makeText(this, "Danh sách sinh viên trống", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] items = new String[sinhVienList.size()];
        for (int i = 0; i < sinhVienList.size(); i++) {
            SinhVien sv = sinhVienList.get(i);
            items[i] = sv.getMaSV() + " - " + sv.getHoTen() + " (" + sv.getLop() + ")";
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Chọn sinh viên")
                .setItems(items, (dialog, which) -> {
                    SinhVien selected = sinhVienList.get(which);
                    edtMaSV.setText(selected.getMaSV());
                })
                .show();
    }

    private void showMonHocDialog() {
        if (monHocList.isEmpty()) {
            Toast.makeText(this, "Danh sách môn học trống", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] items = new String[monHocList.size()];
        for (int i = 0; i < monHocList.size(); i++) {
            MonHoc mh = monHocList.get(i);
            items[i] = mh.getMaMH() + " - " + mh.getTenMon();
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Chọn môn học")
                .setItems(items, (dialog, which) -> {
                    MonHoc selected = monHocList.get(which);
                    edtMaMH.setText(selected.getMaMH());
                })
                .show();
    }
}

