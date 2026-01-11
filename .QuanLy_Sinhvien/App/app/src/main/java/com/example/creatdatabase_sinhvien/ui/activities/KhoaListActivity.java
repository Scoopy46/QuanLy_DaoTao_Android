package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.adapters.KhoaAdapter;
import com.example.creatdatabase_sinhvien.models.Khoa;
import com.example.creatdatabase_sinhvien.repositories.KhoaRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity hiển thị danh sách khoa
 */
public class KhoaListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewKhoa;
    private FloatingActionButton fabAdd;
    private Button btnBack;
    
    private KhoaRepository khoaRepository;
    private KhoaAdapter adapter;
    private List<Khoa> khoaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa_list);

        initViews();
        setupListeners();
        
        khoaRepository = new KhoaRepository();
        khoaList = new ArrayList<>();
        
        // Setup RecyclerView
        adapter = new KhoaAdapter(khoaList);
        adapter.setOnItemClickListener(khoa -> {
            // Chuyển sang màn hình cập nhật
            openUpdateActivity(khoa);
        });
        recyclerViewKhoa.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewKhoa.setAdapter(adapter);
        recyclerViewKhoa.setHasFixedSize(false);
        recyclerViewKhoa.setNestedScrollingEnabled(true);

        // Load danh sách khoa
        loadKhoaList();
    }

    private void initViews() {
        recyclerViewKhoa = findViewById(R.id.recyclerViewKhoa);
        fabAdd = findViewById(R.id.fabAdd);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> {
            // Chuyển sang màn hình thêm mới
            android.content.Intent intent = new android.content.Intent(this, AddKhoaActivity.class);
            startActivity(intent);
        });
        
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadKhoaList() {
        android.util.Log.d("KhoaListActivity", "Loading Khoa list...");
        Toast.makeText(this, "Đang tải danh sách khoa...", Toast.LENGTH_SHORT).show();
        
        khoaRepository.getAllKhoa(new KhoaRepository.KhoaCallback() {
            @Override
            public void onSuccess(List<Khoa> khoas) {
                runOnUiThread(() -> {
                    android.util.Log.d("KhoaListActivity", "Received " + khoas.size() + " Khoa");
                    khoaList.clear();
                    if (khoas != null && !khoas.isEmpty()) {
                        khoaList.addAll(khoas);
                        adapter.updateList(khoaList);
                        Toast.makeText(KhoaListActivity.this, "Đã tải " + khoas.size() + " khoa", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.updateList(khoaList);
                        Toast.makeText(KhoaListActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    android.util.Log.e("KhoaListActivity", "Error loading: " + error);
                    Toast.makeText(KhoaListActivity.this, "Lỗi tải danh sách khoa: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void openUpdateActivity(Khoa khoa) {
        android.content.Intent intent = new android.content.Intent(this, UpdateKhoaActivity.class);
        intent.putExtra("KHOA", khoa);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách khi quay lại màn hình
        loadKhoaList();
    }
}

