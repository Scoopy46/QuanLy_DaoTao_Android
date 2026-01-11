package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.adapters.NganhAdapter;
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.models.Nganh;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import com.example.creatdatabase_sinhvien.repositories.NganhRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity hiển thị danh sách ngành học
 */
public class NganhListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNganh;
    private FloatingActionButton fabAdd;
    private Button btnBack;
    
    private NganhRepository nganhRepository;
    private KhoaRepository khoaRepository;
    private NganhAdapter adapter;
    private List<Nganh> nganhList;
    private List<Khoa> khoaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nganh_list);

        initViews();
        setupListeners();
        
        nganhRepository = new NganhRepository();
        khoaRepository = new KhoaRepository();
        nganhList = new ArrayList<>();
        khoaList = new ArrayList<>();
        
        // Load danh sách khoa trước để map tenKhoa
        loadKhoaList();
        
        // Setup RecyclerView
        adapter = new NganhAdapter(nganhList);
        adapter.setOnItemClickListener(nganh -> {
            // Chuyển sang màn hình cập nhật
            openUpdateActivity(nganh);
        });
        adapter.setOnItemLongClickListener(nganh -> {
            // Hiển thị dialog xác nhận xóa
            showDeleteConfirmation(nganh);
            return true;
        });
        recyclerViewNganh.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNganh.setAdapter(adapter);
        recyclerViewNganh.setHasFixedSize(false);
        recyclerViewNganh.setNestedScrollingEnabled(true);
    }

    private void loadKhoaList() {
        khoaRepository.getAllKhoa(new KhoaRepository.KhoaCallback() {
            @Override
            public void onSuccess(List<Khoa> khoas) {
                runOnUiThread(() -> {
                    khoaList = khoas;
                    // Sau khi load xong khoa, load danh sách ngành
                    loadNganhList();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    android.util.Log.e("NganhListActivity", "Error loading khoa: " + error);
                    // Vẫn load ngành dù không có khoa
                    loadNganhList();
                });
            }
        });
    }

    private void initViews() {
        recyclerViewNganh = findViewById(R.id.recyclerViewNganh);
        fabAdd = findViewById(R.id.fabAdd);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> {
            // Chuyển sang màn hình thêm mới
            android.content.Intent intent = new android.content.Intent(this, AddNganhActivity.class);
            startActivity(intent);
        });
        
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadNganhList() {
        android.util.Log.d("NganhListActivity", "Loading Nganh list...");
        Toast.makeText(this, "Đang tải danh sách ngành...", Toast.LENGTH_SHORT).show();
        
        nganhRepository.getAllNganh(new NganhRepository.NganhCallback() {
            @Override
            public void onSuccess(List<Nganh> nganhs) {
                runOnUiThread(() -> {
                    android.util.Log.d("NganhListActivity", "Received " + nganhs.size() + " Nganh");
                    nganhList.clear();
                    if (nganhs != null && !nganhs.isEmpty()) {
                        // Map tenKhoa từ danh sách Khoa
                        for (Nganh nganh : nganhs) {
                            if (nganh.getTenKhoa() == null || nganh.getTenKhoa().isEmpty()) {
                                // Tìm tenKhoa từ maKhoa
                                if (nganh.getMaKhoa() != null && !nganh.getMaKhoa().isEmpty() && khoaList != null) {
                                    for (Khoa khoa : khoaList) {
                                        if (khoa.getMaKhoa().equals(nganh.getMaKhoa())) {
                                            nganh.setTenKhoa(khoa.getTenKhoa());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        nganhList.addAll(nganhs);
                        adapter.updateList(nganhList);
                        Toast.makeText(NganhListActivity.this, "Đã tải " + nganhs.size() + " ngành", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.updateList(nganhList);
                        Toast.makeText(NganhListActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    android.util.Log.e("NganhListActivity", "Error loading: " + error);
                    Toast.makeText(NganhListActivity.this, "Lỗi tải danh sách ngành: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void openUpdateActivity(Nganh nganh) {
        android.content.Intent intent = new android.content.Intent(this, UpdateNganhActivity.class);
        intent.putExtra("NGANH", nganh);
        startActivity(intent);
    }

    private void showDeleteConfirmation(Nganh nganh) {
        String nganhName = nganh.getTenNganh() != null ? nganh.getTenNganh() : nganh.getMaNganh();
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa ngành " + nganhName + " không?")
            .setPositiveButton("Đồng ý", (dialog, which) -> deleteNganh(nganh))
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteNganh(Nganh nganh) {
        nganhRepository.deleteNganh(nganh.getMaNganh(), new NganhRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(NganhListActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadNganhList();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(NganhListActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách khi quay lại màn hình
        loadNganhList();
    }
}

