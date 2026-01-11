package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.adapters.GiaoVienAdapter;
import com.example.creatdatabase_sinhvien.models.GiaoVien;
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.repositories.GiaoVienRepository;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity hiển thị danh sách giáo viên với bộ lọc theo khoa
 */
public class GiaoVienListActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvBackToMain;
    private Spinner spinnerKhoa;
    private RecyclerView recyclerViewGiaoVien;
    private FloatingActionButton fabAdd;
    
    private GiaoVienRepository giaoVienRepository;
    private KhoaRepository khoaRepository;
    private GiaoVienAdapter adapter;
    private List<GiaoVien> giaoVienList;
    private List<Khoa> khoaList;
    private String selectedMaKhoa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giaovien_list);

        initViews();
        setupListeners();
        
        giaoVienRepository = new GiaoVienRepository();
        khoaRepository = new KhoaRepository();
        giaoVienList = new ArrayList<>();
        
        // Setup RecyclerView
        adapter = new GiaoVienAdapter(this, giaoVienList);
        adapter.setOnItemClickListener(giaoVien -> {
            // Chuyển sang màn hình cập nhật
            openUpdateActivity(giaoVien);
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGiaoVien.setLayoutManager(layoutManager);
        recyclerViewGiaoVien.setAdapter(adapter);
        recyclerViewGiaoVien.setHasFixedSize(false);
        recyclerViewGiaoVien.setNestedScrollingEnabled(true);

        // Load danh sách khoa và giáo viên
        android.util.Log.d("GiaoVienListActivity", "onCreate - Starting to load data...");
        loadKhoaList();
        
        // Load danh sách giáo viên ngay lập tức (không đợi khoa)
        android.util.Log.d("GiaoVienListActivity", "onCreate - Loading GiaoVien list immediately...");
        loadGiaoVienList();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvBackToMain = findViewById(R.id.tvBackToMain);
        spinnerKhoa = findViewById(R.id.spinnerKhoa);
        recyclerViewGiaoVien = findViewById(R.id.recyclerViewGiaoVien);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupListeners() {
        tvBackToMain.setOnClickListener(v -> finish());
        
        fabAdd.setOnClickListener(v -> {
            // Chuyển sang màn hình thêm mới
            android.content.Intent intent = new android.content.Intent(this, AddGiaoVienActivity.class);
            startActivity(intent);
        });

        spinnerKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && khoaList != null && position <= khoaList.size()) {
                    Khoa selectedKhoa = khoaList.get(position - 1);
                    selectedMaKhoa = selectedKhoa.getMaKhoa();
                } else {
                    selectedMaKhoa = ""; // Tất cả
                }
                loadGiaoVienList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMaKhoa = "";
                loadGiaoVienList();
            }
        });
    }

    private void loadKhoaList() {
        android.util.Log.d("GiaoVienListActivity", "loadKhoaList - Starting...");
        khoaRepository.getAllKhoa(new KhoaRepository.KhoaCallback() {
            @Override
            public void onSuccess(List<Khoa> khoas) {
                runOnUiThread(() -> {
                    khoaList = khoas;
                    setupKhoaSpinner();
                    loadGiaoVienList();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(GiaoVienListActivity.this, "Lỗi tải danh sách khoa: " + error, Toast.LENGTH_SHORT).show();
                    // Vẫn load danh sách giáo viên không lọc
                    loadGiaoVienList();
                });
            }
        });
    }

    private void setupKhoaSpinner() {
        List<String> khoaNames = new ArrayList<>();
        khoaNames.add("Tất cả"); // Option đầu tiên
        
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

    private void loadGiaoVienList() {
        android.util.Log.d("GiaoVienListActivity", "loadGiaoVienList - Called with maKhoa: " + selectedMaKhoa);
        android.util.Log.d("GiaoVienListActivity", "loadGiaoVienList - Repository: " + (giaoVienRepository != null ? "OK" : "NULL"));
        Toast.makeText(this, "Đang tải danh sách giáo viên...", Toast.LENGTH_SHORT).show();
        
        if (giaoVienRepository == null) {
            android.util.Log.e("GiaoVienListActivity", "giaoVienRepository is NULL!");
            Toast.makeText(this, "Lỗi: Repository chưa được khởi tạo", Toast.LENGTH_SHORT).show();
            return;
        }
        
        android.util.Log.d("GiaoVienListActivity", "loadGiaoVienList - Calling getAllGiaoVien...");
        giaoVienRepository.getAllGiaoVien(selectedMaKhoa, new GiaoVienRepository.GiaoVienCallback() {
            @Override
            public void onSuccess(List<GiaoVien> giaoViens) {
                runOnUiThread(() -> {
                    android.util.Log.d("GiaoVienListActivity", "Received " + giaoViens.size() + " GiaoVien");
                    giaoVienList.clear();
                    if (giaoViens != null && !giaoViens.isEmpty()) {
                        giaoVienList.addAll(giaoViens);
                        adapter.updateList(giaoVienList);
                        Toast.makeText(GiaoVienListActivity.this, "Đã tải " + giaoViens.size() + " giáo viên", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.updateList(giaoVienList);
                        Toast.makeText(GiaoVienListActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    android.util.Log.e("GiaoVienListActivity", "Error loading: " + error);
                    Toast.makeText(GiaoVienListActivity.this, "Lỗi tải danh sách giáo viên: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void openUpdateActivity(GiaoVien giaoVien) {
        android.content.Intent intent = new android.content.Intent(this, UpdateGiaoVienActivity.class);
        intent.putExtra("GIAO_VIEN", giaoVien);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách khi quay lại màn hình
        loadGiaoVienList();
    }
}

