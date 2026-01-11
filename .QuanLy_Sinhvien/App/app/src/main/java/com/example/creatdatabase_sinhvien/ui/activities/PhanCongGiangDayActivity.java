package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.adapters.PhanCongGiangDayAdapter;
import com.example.creatdatabase_sinhvien.models.GiaoVien;
import com.example.creatdatabase_sinhvien.models.Lop;
import com.example.creatdatabase_sinhvien.models.MonHoc;
import com.example.creatdatabase_sinhvien.models.PhanCongGiangDay;
import com.example.creatdatabase_sinhvien.repositories.GiaoVienRepository;
import com.example.creatdatabase_sinhvien.repositories.LopRepository;
import com.example.creatdatabase_sinhvien.repositories.MonHocRepository;
import com.example.creatdatabase_sinhvien.repositories.PhanCongGiangDayRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Module: Phân công giảng dạy
 *
 * - Chọn Lớp -> GET /api/PhanCongGiangDay?maLop=...
 * - Thêm / Cập nhật -> POST /api/PhanCongGiangDay (body: maLop, maMH, maGV)
 * - Xóa -> DELETE /api/PhanCongGiangDay?maLop=...&maMH=...
 */
public class PhanCongGiangDayActivity extends AppCompatActivity {
    private Spinner spinnerLop;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private TextView tvBack;

    private LopRepository lopRepository;
    private MonHocRepository monHocRepository;
    private GiaoVienRepository giaoVienRepository;
    private PhanCongGiangDayRepository pcgdRepository;

    private final List<Lop> lopList = new ArrayList<>();
    private final List<MonHoc> monHocList = new ArrayList<>();
    private final List<GiaoVien> giaoVienList = new ArrayList<>();
    private final List<PhanCongGiangDay> pcgdList = new ArrayList<>();

    private PhanCongGiangDayAdapter adapter;

    private Lop selectedLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phan_cong_giang_day);

        initViews();
        setupRecyclerView();
        setupListeners();

        lopRepository = new LopRepository();
        monHocRepository = new MonHocRepository();
        giaoVienRepository = new GiaoVienRepository();
        pcgdRepository = new PhanCongGiangDayRepository();

        loadSupportData();
    }

    private void initViews() {
        spinnerLop = findViewById(R.id.spinnerLop);
        recyclerView = findViewById(R.id.recyclerViewPhanCong);
        fabAdd = findViewById(R.id.fabAdd);
        tvBack = findViewById(R.id.tvBack);
    }

    private void setupRecyclerView() {
        adapter = new PhanCongGiangDayAdapter(this, pcgdList);
        adapter.setOnItemClickListener(this::showUpdateDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
    }

    private void setupListeners() {
        tvBack.setOnClickListener(v -> finish());
        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void loadSupportData() {
        Toast.makeText(this, "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show();
        loadLopList();
        loadMonHocList();
        loadGiaoVienList();
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
                runOnUiThread(() ->
                        Toast.makeText(PhanCongGiangDayActivity.this, "Lỗi tải danh sách lớp: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void setupLopSpinner() {
        List<String> lopNames = new ArrayList<>();
        lopNames.add("-- Chọn lớp --");
        for (Lop lop : lopList) {
            String name = lop.getTenLop() != null && !lop.getTenLop().isEmpty() ? lop.getTenLop() : lop.getMaLop();
            lopNames.add(name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lopNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(adapter);

        spinnerLop.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position <= 0) {
                    selectedLop = null;
                    pcgdList.clear();
                    PhanCongGiangDayActivity.this.adapter.updateList(pcgdList);
                    return;
                }
                selectedLop = lopList.get(position - 1);
                android.util.Log.d("PhanCongGiangDay", "Lop selected - maLop: '" + (selectedLop != null ? selectedLop.getMaLop() : "null") + "', tenLop: '" + (selectedLop != null ? selectedLop.getTenLop() : "null") + "'");
                if (selectedLop != null && (selectedLop.getMaLop() == null || selectedLop.getMaLop().trim().isEmpty())) {
                    android.util.Log.e("PhanCongGiangDay", "WARNING: selectedLop has empty maLop! selectedLop=" + selectedLop);
                }
                loadPhanCongBySelectedLop();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedLop = null;
            }
        });
    }

    private void loadMonHocList() {
        monHocRepository.getAllMonHoc(new MonHocRepository.MonHocCallback() {
            @Override
            public void onSuccess(List<MonHoc> monHocs) {
                runOnUiThread(() -> {
                    monHocList.clear();
                    if (monHocs != null) monHocList.addAll(monHocs);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(PhanCongGiangDayActivity.this, "Lỗi tải danh sách môn học: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void loadGiaoVienList() {
        // lấy toàn bộ (không lọc khoa)
        giaoVienRepository.getAllGiaoVien(null, new GiaoVienRepository.GiaoVienCallback() {
            @Override
            public void onSuccess(List<GiaoVien> giaoViens) {
                runOnUiThread(() -> {
                    giaoVienList.clear();
                    if (giaoViens != null) giaoVienList.addAll(giaoViens);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(PhanCongGiangDayActivity.this, "Lỗi tải danh sách giáo viên: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void loadPhanCongBySelectedLop() {
        if (selectedLop == null || selectedLop.getMaLop() == null || selectedLop.getMaLop().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn lớp", Toast.LENGTH_SHORT).show();
            return;
        }
        String maLop = selectedLop.getMaLop();
        Toast.makeText(this, "Đang tải phân công...", Toast.LENGTH_SHORT).show();
        pcgdRepository.getByLop(maLop, new PhanCongGiangDayRepository.PhanCongCallback() {
            @Override
            public void onSuccess(List<PhanCongGiangDay> list) {
                runOnUiThread(() -> {
                    pcgdList.clear();
                    if (list != null) pcgdList.addAll(list);
                    adapter.updateList(pcgdList);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(PhanCongGiangDayActivity.this, "Lỗi tải phân công: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void showAddDialog() {
        if (selectedLop == null) {
            Toast.makeText(this, "Vui lòng chọn lớp trước", Toast.LENGTH_SHORT).show();
            return;
        }
        if (monHocList.isEmpty()) {
            Toast.makeText(this, "Danh sách môn học trống (chờ tải dữ liệu)", Toast.LENGTH_SHORT).show();
            return;
        }
        if (giaoVienList.isEmpty()) {
            Toast.makeText(this, "Danh sách giáo viên trống (chờ tải dữ liệu)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu maLop vào biến local để đảm bảo không bị mất
        final String savedMaLop = selectedLop.getMaLop();
        if (savedMaLop == null || savedMaLop.trim().isEmpty()) {
            android.util.Log.e("PhanCongGiangDay", "ERROR: savedMaLop is null or empty! selectedLop=" + selectedLop);
            Toast.makeText(this, "Lỗi: Mã lớp không hợp lệ. Vui lòng chọn lại lớp.", Toast.LENGTH_LONG).show();
            return;
        }
        
        android.util.Log.d("PhanCongGiangDay", "showAddDialog - savedMaLop: '" + savedMaLop + "'");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_phan_cong_giang_day, null);
        TextView tvLop = view.findViewById(R.id.tvLopValue);
        Spinner spMon = view.findViewById(R.id.spinnerMonHoc);
        Spinner spGV = view.findViewById(R.id.spinnerGiaoVien);
        Button btnHuy = view.findViewById(R.id.btnHuy);
        Button btnLuu = view.findViewById(R.id.btnLuu);

        String lopName = selectedLop.getTenLop() != null && !selectedLop.getTenLop().isEmpty()
                ? selectedLop.getTenLop()
                : selectedLop.getMaLop();
        tvLop.setText(lopName);

        setupMonHocSpinner(spMon);
        setupGiaoVienSpinner(spGV, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        btnLuu.setOnClickListener(v -> {
            int posMon = spMon.getSelectedItemPosition();
            int posGV = spGV.getSelectedItemPosition();
            if (posMon <= 0) {
                Toast.makeText(this, "Vui lòng chọn môn học", Toast.LENGTH_SHORT).show();
                return;
            }
            if (posGV <= 0) {
                Toast.makeText(this, "Vui lòng chọn giáo viên", Toast.LENGTH_SHORT).show();
                return;
            }

            MonHoc mh = monHocList.get(posMon - 1);
            GiaoVien gv = giaoVienList.get(posGV - 1);

            // Validate objects
            if (mh == null) {
                Toast.makeText(this, "Lỗi: Môn học không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (gv == null) {
                Toast.makeText(this, "Lỗi: Giáo viên không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sử dụng savedMaLop đã lưu trước đó
            String maLop = savedMaLop;
            String maMH = mh.getMaMH();
            String maGV = gv.getMaGV();

            // Log giá trị gốc trước khi validate
            android.util.Log.d("PhanCongGiangDay", "Raw values - maLop: '" + maLop + "' (null? " + (maLop == null) + "), maMH: '" + maMH + "', maGV: '" + maGV + "'");
            android.util.Log.d("PhanCongGiangDay", "selectedLop object: " + selectedLop);
            android.util.Log.d("PhanCongGiangDay", "mh object: " + mh);
            android.util.Log.d("PhanCongGiangDay", "gv object: " + gv);

            // Validate các giá trị không được null hoặc empty
            // Lưu ý: các model có thể trả về empty string "" thay vì null
            if (maLop == null || maLop.trim().isEmpty()) {
                android.util.Log.e("PhanCongGiangDay", "ERROR: maLop is null or empty!");
                android.util.Log.e("PhanCongGiangDay", "selectedLop=" + selectedLop);
                android.util.Log.e("PhanCongGiangDay", "selectedLop.getMaLop()=" + maLop);
                Toast.makeText(this, "Lỗi: Mã lớp không hợp lệ. Vui lòng chọn lại lớp.", Toast.LENGTH_LONG).show();
                return;
            }
            if (maMH == null || maMH.trim().isEmpty()) {
                android.util.Log.e("PhanCongGiangDay", "ERROR: maMH is null or empty!");
                android.util.Log.e("PhanCongGiangDay", "mh=" + mh);
                android.util.Log.e("PhanCongGiangDay", "mh.getMaMH()=" + maMH);
                Toast.makeText(this, "Lỗi: Mã môn học không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (maGV == null || maGV.trim().isEmpty()) {
                android.util.Log.e("PhanCongGiangDay", "ERROR: maGV is null or empty!");
                android.util.Log.e("PhanCongGiangDay", "gv=" + gv);
                android.util.Log.e("PhanCongGiangDay", "gv.getMaGV()=" + maGV);
                Toast.makeText(this, "Lỗi: Mã giáo viên không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trim và đảm bảo không phải empty
            maLop = maLop.trim();
            maMH = maMH.trim();
            maGV = maGV.trim();

            // Final check sau khi trim
            if (maLop.isEmpty() || maMH.isEmpty() || maGV.isEmpty()) {
                android.util.Log.e("PhanCongGiangDay", "ERROR: Values are empty after trim!");
                Toast.makeText(this, "Lỗi: Dữ liệu không hợp lệ sau khi xử lý", Toast.LENGTH_SHORT).show();
                return;
            }

            android.util.Log.d("PhanCongGiangDay", "Final values before API call - maLop: '" + maLop + "' (len=" + maLop.length() + "), maMH: '" + maMH + "' (len=" + maMH.length() + "), maGV: '" + maGV + "' (len=" + maGV.length() + ")");
            
            btnLuu.setEnabled(false);
            Toast.makeText(this, "Đang lưu...", Toast.LENGTH_SHORT).show();
            pcgdRepository.save(maLop, maMH, maGV, new PhanCongGiangDayRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(PhanCongGiangDayActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                        loadPhanCongBySelectedLop();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        btnLuu.setEnabled(true);
                        Toast.makeText(PhanCongGiangDayActivity.this, normalizeDuplicateMessage(error), Toast.LENGTH_LONG).show();
                    });
                }
            });
        });

        dialog.show();
    }

    private void showUpdateDialog(PhanCongGiangDay item) {
        if (selectedLop == null || item == null) {
            android.util.Log.e("PhanCongGiangDay", "ERROR: selectedLop or item is null!");
            return;
        }
        if (giaoVienList.isEmpty()) {
            Toast.makeText(this, "Danh sách giáo viên trống (chờ tải dữ liệu)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu maLop và maMH vào biến local để đảm bảo không bị mất
        final String savedMaLop = selectedLop.getMaLop();
        final String savedMaMH = item.getMaMH();
        
        if (savedMaLop == null || savedMaLop.trim().isEmpty()) {
            android.util.Log.e("PhanCongGiangDay", "ERROR: savedMaLop is null or empty! selectedLop=" + selectedLop);
            Toast.makeText(this, "Lỗi: Mã lớp không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (savedMaMH == null || savedMaMH.trim().isEmpty()) {
            android.util.Log.e("PhanCongGiangDay", "ERROR: savedMaMH is null or empty! item=" + item);
            Toast.makeText(this, "Lỗi: Mã môn học không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        
        android.util.Log.d("PhanCongGiangDay", "showUpdateDialog - savedMaLop: '" + savedMaLop + "', savedMaMH: '" + savedMaMH + "'");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_phan_cong_giang_day, null);
        TextView tvMon = view.findViewById(R.id.tvMonValue);
        Spinner spGV = view.findViewById(R.id.spinnerGiaoVien);
        Button btnXoa = view.findViewById(R.id.btnXoa);
        Button btnHuy = view.findViewById(R.id.btnHuy);
        Button btnLuu = view.findViewById(R.id.btnLuu);

        tvMon.setText(item.getTenMon() != null ? item.getTenMon() : "");

        setupGiaoVienSpinner(spGV, item.getMaGV());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLuu.setOnClickListener(v -> {
            int posGV = spGV.getSelectedItemPosition();
            if (posGV <= 0) {
                Toast.makeText(this, "Vui lòng chọn giáo viên", Toast.LENGTH_SHORT).show();
                return;
            }
            GiaoVien gv = giaoVienList.get(posGV - 1);
            
            // Validate objects
            if (gv == null) {
                Toast.makeText(this, "Lỗi: Giáo viên không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Sử dụng savedMaLop và savedMaMH đã lưu trước đó
            String maLop = savedMaLop;
            String maMH = savedMaMH;
            String maGV = gv.getMaGV();

            // Log giá trị gốc trước khi validate
            android.util.Log.d("PhanCongGiangDay", "Update - Raw values - maLop: '" + maLop + "' (null? " + (maLop == null) + "), maMH: '" + maMH + "', maGV: '" + maGV + "'");

            if (maLop == null || maLop.trim().isEmpty()) {
                android.util.Log.e("PhanCongGiangDay", "ERROR: maLop is null or empty!");
                android.util.Log.e("PhanCongGiangDay", "selectedLop=" + selectedLop);
                android.util.Log.e("PhanCongGiangDay", "selectedLop.getMaLop()=" + maLop);
                Toast.makeText(this, "Lỗi: Mã lớp không hợp lệ. Vui lòng chọn lại lớp.", Toast.LENGTH_LONG).show();
                return;
            }
            if (maMH == null || maMH.trim().isEmpty()) {
                android.util.Log.e("PhanCongGiangDay", "ERROR: maMH is null or empty!");
                android.util.Log.e("PhanCongGiangDay", "item=" + item);
                android.util.Log.e("PhanCongGiangDay", "item.getMaMH()=" + maMH);
                Toast.makeText(this, "Lỗi: Mã môn học không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (maGV == null || maGV.trim().isEmpty()) {
                android.util.Log.e("PhanCongGiangDay", "ERROR: maGV is null or empty!");
                android.util.Log.e("PhanCongGiangDay", "gv=" + gv);
                android.util.Log.e("PhanCongGiangDay", "gv.getMaGV()=" + maGV);
                Toast.makeText(this, "Lỗi: Mã giáo viên không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trim và đảm bảo không phải empty
            maLop = maLop.trim();
            maMH = maMH.trim();
            maGV = maGV.trim();

            // Final check sau khi trim
            if (maLop.isEmpty() || maMH.isEmpty() || maGV.isEmpty()) {
                android.util.Log.e("PhanCongGiangDay", "ERROR: Values are empty after trim!");
                Toast.makeText(this, "Lỗi: Dữ liệu không hợp lệ sau khi xử lý", Toast.LENGTH_SHORT).show();
                return;
            }

            android.util.Log.d("PhanCongGiangDay", "Update - Final values - maLop: '" + maLop + "' (len=" + maLop.length() + "), maMH: '" + maMH + "' (len=" + maMH.length() + "), maGV: '" + maGV + "' (len=" + maGV.length() + ")");
            
            btnLuu.setEnabled(false);
            Toast.makeText(this, "Đang cập nhật...", Toast.LENGTH_SHORT).show();
            pcgdRepository.save(maLop, maMH, maGV, new PhanCongGiangDayRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(PhanCongGiangDayActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadPhanCongBySelectedLop();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        btnLuu.setEnabled(true);
                        Toast.makeText(PhanCongGiangDayActivity.this, "Lỗi cập nhật: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        });

        btnXoa.setOnClickListener(v -> {
            String tenMon = item.getTenMon() != null ? item.getTenMon() : item.getMaMH();
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa môn \"" + tenMon + "\" khỏi lớp không?")
                    .setPositiveButton("Xóa", (d, which) -> doDelete(item, dialog))
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        dialog.show();
    }

    private void doDelete(PhanCongGiangDay item, AlertDialog parentDialog) {
        if (selectedLop == null || item == null) {
            android.util.Log.e("PhanCongGiangDay", "ERROR: selectedLop or item is null in doDelete!");
            return;
        }
        String maLop = selectedLop.getMaLop();
        String maMH = item.getMaMH();
        if (maLop == null || maLop.trim().isEmpty() || maMH == null || maMH.trim().isEmpty()) {
            android.util.Log.e("PhanCongGiangDay", "ERROR: maLop or maMH is null/empty in doDelete!");
            Toast.makeText(this, "Lỗi: Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        pcgdRepository.delete(maLop.trim(), maMH.trim(), new PhanCongGiangDayRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    parentDialog.dismiss();
                    Toast.makeText(PhanCongGiangDayActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadPhanCongBySelectedLop();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(PhanCongGiangDayActivity.this, "Lỗi xóa: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void setupMonHocSpinner(Spinner spinner) {
        List<String> names = new ArrayList<>();
        names.add("-- Chọn môn học --");
        for (MonHoc mh : monHocList) {
            String display = (mh.getTenMon() != null && !mh.getTenMon().isEmpty())
                    ? mh.getTenMon() + " (" + (mh.getSoTinChi() != null ? mh.getSoTinChi() : 0) + " TC)"
                    : mh.getMaMH();
            names.add(display);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupGiaoVienSpinner(Spinner spinner, String selectedMaGV) {
        List<String> names = new ArrayList<>();
        names.add("-- Chọn giáo viên --");
        for (GiaoVien gv : giaoVienList) {
            String display = gv.getHoten() != null && !gv.getHoten().isEmpty()
                    ? gv.getHoten()
                    : gv.getMaGV();
            names.add(display);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (selectedMaGV != null && !selectedMaGV.isEmpty()) {
            int index = findGiaoVienIndexByMaGV(selectedMaGV);
            if (index >= 0) spinner.setSelection(index + 1); // +1 do có placeholder
        }
    }

    private int findGiaoVienIndexByMaGV(String maGV) {
        for (int i = 0; i < giaoVienList.size(); i++) {
            GiaoVien gv = giaoVienList.get(i);
            if (gv != null && gv.getMaGV() != null && gv.getMaGV().equals(maGV)) return i;
        }
        return -1;
    }

    /**
     * Theo yêu cầu: nếu thêm mới một môn đã có trong lớp -> báo "Môn học này đã được phân công rồi"
     */
    private String normalizeDuplicateMessage(String error) {
        if (error == null) return "Lỗi";
        String lower = error.toLowerCase(Locale.ROOT);
        boolean looksLikeDuplicate = lower.contains("duplicate")
                || lower.contains("already")
                || lower.contains("đã")
                || (lower.contains("409"))
                || (lower.contains("conflict"));
        if (looksLikeDuplicate) {
            return "Môn học này đã được phân công rồi";
        }
        return "Lỗi: " + error;
    }
}


