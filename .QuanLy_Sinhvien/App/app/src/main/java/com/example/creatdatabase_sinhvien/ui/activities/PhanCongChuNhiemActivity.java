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
import com.example.creatdatabase_sinhvien.adapters.PhanCongChuNhiemAdapter;
import com.example.creatdatabase_sinhvien.models.GiaoVien;
import com.example.creatdatabase_sinhvien.models.PhanCongChuNhiem;
import com.example.creatdatabase_sinhvien.repositories.GiaoVienRepository;
import com.example.creatdatabase_sinhvien.repositories.PhanCongChuNhiemRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Module: Phân công Chủ nhiệm (GVCN)
 *
 * - GET /api/PhanCongChuNhiem: danh sách lớp + trạng thái GVCN
 * - POST /api/PhanCongChuNhiem?maLop=...&maGV=...: cập nhật GVCN (không gửi ngày)
 */
public class PhanCongChuNhiemActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvBack;

    private PhanCongChuNhiemRepository repository;
    private GiaoVienRepository giaoVienRepository;

    private final List<PhanCongChuNhiem> lopStatusList = new ArrayList<>();
    private final List<GiaoVien> giaoVienList = new ArrayList<>();

    private PhanCongChuNhiemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phan_cong_chu_nhiem);

        recyclerView = findViewById(R.id.recyclerViewPccn);
        tvBack = findViewById(R.id.tvBack);

        repository = new PhanCongChuNhiemRepository();
        giaoVienRepository = new GiaoVienRepository();

        adapter = new PhanCongChuNhiemAdapter(this, lopStatusList);
        adapter.setOnUpdateClickListener(this::showUpdateDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        tvBack.setOnClickListener(v -> finish());

        loadGiaoVienList();
        loadList();
    }

    private void loadList() {
        Toast.makeText(this, "Đang tải danh sách...", Toast.LENGTH_SHORT).show();
        repository.getAll(new PhanCongChuNhiemRepository.ListCallback() {
            @Override
            public void onSuccess(List<PhanCongChuNhiem> list) {
                runOnUiThread(() -> {
                    lopStatusList.clear();
                    if (list != null) lopStatusList.addAll(list);
                    adapter.updateList(lopStatusList);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(PhanCongChuNhiemActivity.this, "Lỗi tải danh sách: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void loadGiaoVienList() {
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
                        Toast.makeText(PhanCongChuNhiemActivity.this, "Lỗi tải danh sách giáo viên: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void showUpdateDialog(PhanCongChuNhiem item) {
        if (item == null) return;
        if (giaoVienList.isEmpty()) {
            Toast.makeText(this, "Danh sách giáo viên trống (chờ tải dữ liệu)", Toast.LENGTH_SHORT).show();
            return;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_phan_cong_chu_nhiem, null);
        TextView tvLop = view.findViewById(R.id.tvLopValue);
        Spinner spinnerGV = view.findViewById(R.id.spinnerGiaoVien);
        Button btnHuy = view.findViewById(R.id.btnHuy);
        Button btnLuu = view.findViewById(R.id.btnLuu);

        String lopName = item.getTenLop() != null && !item.getTenLop().isEmpty() ? item.getTenLop() : item.getMaLop();
        tvLop.setText(lopName != null ? lopName : "");

        setupGiaoVienSpinner(spinnerGV, item.getMaGV());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        btnLuu.setOnClickListener(v -> {
            int pos = spinnerGV.getSelectedItemPosition();
            if (pos <= 0) {
                Toast.makeText(this, "Vui lòng chọn giáo viên", Toast.LENGTH_SHORT).show();
                return;
            }
            GiaoVien gv = giaoVienList.get(pos - 1);
            String maGV = gv != null ? gv.getMaGV() : "";
            if (maGV == null || maGV.trim().isEmpty()) {
                Toast.makeText(this, "Mã giáo viên không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            String maLop = item.getMaLop();
            if (maLop == null || maLop.trim().isEmpty()) {
                Toast.makeText(this, "Mã lớp không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            btnLuu.setEnabled(false);
            Toast.makeText(this, "Đang lưu thay đổi...", Toast.LENGTH_SHORT).show();
            repository.update(maLop, maGV, new PhanCongChuNhiemRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(PhanCongChuNhiemActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                        loadList();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        btnLuu.setEnabled(true);
                        Toast.makeText(PhanCongChuNhiemActivity.this, "Lỗi lưu: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        });

        dialog.show();
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

        // setSelection đúng GV hiện tại (nếu có)
        if (selectedMaGV != null && !selectedMaGV.isEmpty()) {
            int idx = findGiaoVienIndexByMaGV(selectedMaGV);
            if (idx >= 0) spinner.setSelection(idx + 1); // +1 do placeholder
        }
    }

    private int findGiaoVienIndexByMaGV(String maGV) {
        for (int i = 0; i < giaoVienList.size(); i++) {
            GiaoVien gv = giaoVienList.get(i);
            if (gv != null && gv.getMaGV() != null && gv.getMaGV().equals(maGV)) return i;
        }
        return -1;
    }
}


