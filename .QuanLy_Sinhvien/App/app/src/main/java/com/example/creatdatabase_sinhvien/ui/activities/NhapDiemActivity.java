package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.adapters.DiemThiAdapter;
import com.example.creatdatabase_sinhvien.models.DiemThi;
import com.example.creatdatabase_sinhvien.models.Lop;
import com.example.creatdatabase_sinhvien.models.MonHoc;
import com.example.creatdatabase_sinhvien.models.PhanCongGiangDay;
import com.example.creatdatabase_sinhvien.models.SinhVien;
import com.example.creatdatabase_sinhvien.repositories.DiemThiRepository;
import com.example.creatdatabase_sinhvien.repositories.LopRepository;
import com.example.creatdatabase_sinhvien.repositories.PhanCongGiangDayRepository;
import com.example.creatdatabase_sinhvien.repositories.SinhVienRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity để nhập điểm thi cho sinh viên
 */
public class NhapDiemActivity extends AppCompatActivity {
    private EditText edtMaSV, edtMaMH, edtDiemLan1, edtDiemLan2;
    private Spinner spinnerLop, spinnerMonHoc;
    private Button btnLuuDiem, btnHuy, btnXoaDiem, btnChonSinhVien, btnChonMonHoc;
    private ListView listViewDiemThi;
    
    private DiemThiRepository repository;
    private SinhVienRepository sinhVienRepository;
    private LopRepository lopRepository;
    private PhanCongGiangDayRepository phanCongGiangDayRepository;
    private DiemThiAdapter adapter;
    private List<DiemThi> diemThiList;
    private List<SinhVien> sinhVienList;
    private List<MonHoc> monHocList;
    private List<Lop> lopList;
    private DiemThi selectedDiemThi;

    private Lop selectedLop;
    private MonHoc selectedMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_diem);

        initViews();
        setupListeners();
        
        repository = new DiemThiRepository();
        sinhVienRepository = new SinhVienRepository();
        lopRepository = new LopRepository();
        phanCongGiangDayRepository = new PhanCongGiangDayRepository();
        diemThiList = new ArrayList<>();
        sinhVienList = new ArrayList<>();
        monHocList = new ArrayList<>();
        lopList = new ArrayList<>();
        adapter = new DiemThiAdapter(this, diemThiList);
        listViewDiemThi.setAdapter(adapter);

        loadSinhVienList();
        loadLopList();
        // Môn học sẽ được load theo lớp đã chọn
        setupMonHocSpinner(false);
    }
    
    private void initViews() {
        spinnerLop = findViewById(R.id.spinnerLop);
        spinnerMonHoc = findViewById(R.id.spinnerMonHoc);
        edtMaSV = findViewById(R.id.edtMaSV);
        edtMaMH = findViewById(R.id.edtMaMH);
        edtDiemLan1 = findViewById(R.id.edtDiemLan1);
        edtDiemLan2 = findViewById(R.id.edtDiemLan2);
        btnLuuDiem = findViewById(R.id.btnLuuDiem);
        btnHuy = findViewById(R.id.btnHuy);
        btnXoaDiem = findViewById(R.id.btnXoaDiem);
        btnChonSinhVien = findViewById(R.id.btnChonSinhVien);
        btnChonMonHoc = findViewById(R.id.btnChonMonHoc);
        listViewDiemThi = findViewById(R.id.listViewDiemThi);
    }

    private void setupListeners() {
        btnLuuDiem.setOnClickListener(v -> handleLuuDiem());
        btnHuy.setOnClickListener(v -> clearForm());
        btnXoaDiem.setOnClickListener(v -> handleXoaDiem());
        btnChonSinhVien.setOnClickListener(v -> showSinhVienDialog());
        btnChonMonHoc.setOnClickListener(v -> showMonHocDialog());

        listViewDiemThi.setOnItemClickListener((parent, view, position, id) -> {
            selectedDiemThi = diemThiList.get(position);
            fillForm(selectedDiemThi);
        });

        spinnerLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position <= 0) {
                    selectedLop = null;
                    selectedMonHoc = null;
                    monHocList.clear();
                    setupMonHocSpinner(false);
                    refreshIfReady();
                    return;
                }
                selectedLop = lopList.get(position - 1);
                selectedMonHoc = null;
                monHocList.clear();
                setupMonHocSpinner(false);
                loadMonHocByLop(selectedLop.getMaLop());
                refreshIfReady();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLop = null;
            }
        });

        spinnerMonHoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position <= 0) {
                    selectedMonHoc = null;
                    refreshIfReady();
                    return;
                }
                selectedMonHoc = monHocList.get(position - 1);
                // set mã môn vào form cho tiện thao tác lưu
                if (selectedMonHoc != null) {
                    edtMaMH.setText(selectedMonHoc.getMaMH() != null ? selectedMonHoc.getMaMH() : "");
                }
                refreshIfReady();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMonHoc = null;
            }
        });
    }

    private void loadDiemThiList(String maLop, String maMH) {
        Toast.makeText(this, "Đang tải danh sách điểm thi...", Toast.LENGTH_SHORT).show();
        repository.getDiemThi(
            maLop == null || maLop.isEmpty() ? null : maLop,
            maMH == null || maMH.isEmpty() ? null : maMH,
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

    private void refreshIfReady() {
        // yêu cầu: chọn lớp rồi chọn môn -> hiển thị ra danh sách SV/điểm
        if (selectedLop == null || selectedMonHoc == null) {
            diemThiList = new ArrayList<>();
            adapter.updateList(diemThiList);
            return;
        }
        loadDiemThiList(selectedLop.getMaLop(), selectedMonHoc.getMaMH());
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
                refreshIfReady();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(NhapDiemActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Xóa điểm (gỡ điểm): gửi POST với diemLan1=null, diemLan2=null.
     * Lưu ý: Chỉ hoạt động nếu backend cho phép.
     */
    private void handleXoaDiem() {
        if (selectedDiemThi == null) {
            Toast.makeText(this, "Vui lòng chọn 1 sinh viên trong danh sách để xóa điểm", Toast.LENGTH_SHORT).show();
            return;
        }
        String maSV = selectedDiemThi.getMaSV();
        String maMH = selectedDiemThi.getMaMH();
        if (maSV == null || maSV.trim().isEmpty() || maMH == null || maMH.trim().isEmpty()) {
            Toast.makeText(this, "Không đủ thông tin để xóa điểm", Toast.LENGTH_SHORT).show();
            return;
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn gỡ điểm của SV " + maSV + " (môn " + maMH + ") không?")
                .setPositiveButton("Xóa", (d, which) -> {
                    DiemThi diemThi = new DiemThi();
                    diemThi.setMaSV(maSV);
                    diemThi.setMaMH(maMH);
                    diemThi.setDiemLan1(null);
                    diemThi.setDiemLan2(null);
                    repository.createOrUpdateDiemThi(diemThi, new DiemThiRepository.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(() -> {
                                Toast.makeText(NhapDiemActivity.this, "Đã gỡ điểm", Toast.LENGTH_SHORT).show();
                                clearForm();
                                refreshIfReady();
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> Toast.makeText(NhapDiemActivity.this, "Lỗi xóa điểm: " + error, Toast.LENGTH_LONG).show());
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
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
        // giữ mã môn theo spinner để nhập nhanh
        if (selectedMonHoc != null && selectedMonHoc.getMaMH() != null) {
            edtMaMH.setText(selectedMonHoc.getMaMH());
        } else {
            edtMaMH.setText("");
        }
        edtDiemLan1.setText("");
        edtDiemLan2.setText("");
        selectedDiemThi = null;
    }

    private void loadLopList() {
        lopRepository.getAllLop(new LopRepository.LopCallback() {
            @Override
            public void onSuccess(List<Lop> lops) {
                runOnUiThread(() -> {
                    lopList.clear();
                    if (lops != null) lopList.addAll(lops);
                    setupLopSpinner();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Toast.makeText(NhapDiemActivity.this, "Lỗi tải danh sách lớp: " + error, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupLopSpinner() {
        List<String> names = new ArrayList<>();
        names.add("-- Chọn lớp --");
        for (Lop lop : lopList) {
            String display = lop.getTenLop() != null && !lop.getTenLop().isEmpty() ? lop.getTenLop() : lop.getMaLop();
            names.add(display != null ? display : "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(adapter);
    }

    private void setupMonHocSpinner(boolean enabled) {
        List<String> names = new ArrayList<>();
        names.add("-- Chọn môn --");
        for (MonHoc mh : monHocList) {
            String display = mh.getTenMon() != null && !mh.getTenMon().isEmpty()
                    ? (mh.getTenMon() + " (" + (mh.getSoTinChi() != null ? mh.getSoTinChi() : 0) + " TC)")
                    : mh.getMaMH();
            names.add(display != null ? display : "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonHoc.setAdapter(adapter);
        spinnerMonHoc.setEnabled(enabled);
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

    private void loadMonHocByLop(String maLop) {
        if (maLop == null || maLop.trim().isEmpty()) {
            setupMonHocSpinner(false);
            return;
        }
        Toast.makeText(this, "Đang tải môn theo lớp...", Toast.LENGTH_SHORT).show();
        phanCongGiangDayRepository.getByLop(maLop, new PhanCongGiangDayRepository.PhanCongCallback() {
            @Override
            public void onSuccess(List<PhanCongGiangDay> list) {
                runOnUiThread(() -> {
                    monHocList.clear();
                    if (list != null) {
                        for (PhanCongGiangDay pc : list) {
                            if (pc == null) continue;
                            MonHoc mh = new MonHoc();
                            mh.setMaMH(pc.getMaMH());
                            mh.setTenMon(pc.getTenMon());
                            mh.setSoTinChi(pc.getSoTinChi());
                            monHocList.add(mh);
                        }
                    }

                    if (monHocList.isEmpty()) {
                        Toast.makeText(NhapDiemActivity.this, "Lớp này chưa có môn được phân công", Toast.LENGTH_SHORT).show();
                        setupMonHocSpinner(false);
                        return;
                    }

                    setupMonHocSpinner(true);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(NhapDiemActivity.this, "Lỗi tải môn theo lớp: " + error, Toast.LENGTH_LONG).show();
                    monHocList.clear();
                    setupMonHocSpinner(false);
                });
            }
        });
    }

    private void showSinhVienDialog() {
        if (sinhVienList.isEmpty()) {
            Toast.makeText(this, "Danh sách sinh viên trống", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu đã chọn lớp, chỉ hiển thị SV thuộc lớp đó
        List<SinhVien> source = new ArrayList<>();
        if (selectedLop != null && selectedLop.getMaLop() != null && !selectedLop.getMaLop().isEmpty()) {
            for (SinhVien sv : sinhVienList) {
                if (sv != null && selectedLop.getMaLop().equals(sv.getMaLop())) {
                    source.add(sv);
                }
            }
        } else {
            source.addAll(sinhVienList);
        }

        if (source.isEmpty()) {
            Toast.makeText(this, "Không có sinh viên trong lớp đã chọn", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] items = new String[source.size()];
        for (int i = 0; i < source.size(); i++) {
            SinhVien sv = source.get(i);
            items[i] = sv.getMaSV() + " - " + sv.getHoTen() + " (" + sv.getLop() + ")";
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Chọn sinh viên")
                .setItems(items, (dialog, which) -> {
                    SinhVien selected = source.get(which);
                    edtMaSV.setText(selected.getMaSV());
                })
                .show();
    }

    private void showMonHocDialog() {
        if (selectedLop == null) {
            Toast.makeText(this, "Vui lòng chọn lớp trước", Toast.LENGTH_SHORT).show();
            return;
        }
        if (monHocList.isEmpty()) {
            Toast.makeText(this, "Lớp này chưa có môn để chọn (hoặc đang tải)", Toast.LENGTH_SHORT).show();
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
                    // đồng bộ lại spinner môn học
                    int idx = findMonHocIndexByMaMH(selected.getMaMH());
                    if (idx >= 0) spinnerMonHoc.setSelection(idx + 1);
                })
                .show();
    }

    private int findMonHocIndexByMaMH(String maMH) {
        if (maMH == null) return -1;
        for (int i = 0; i < monHocList.size(); i++) {
            MonHoc mh = monHocList.get(i);
            if (mh != null && maMH.equals(mh.getMaMH())) return i;
        }
        return -1;
    }
}

