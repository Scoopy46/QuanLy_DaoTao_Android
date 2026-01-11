package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.adapters.MonHocAdapter;
import com.example.creatdatabase_sinhvien.models.MonHoc;
import com.example.creatdatabase_sinhvien.repositories.MonHocRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity hiển thị danh sách môn học
 */
public class MonHocListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewMonHoc;
    private FloatingActionButton fabAdd;
    private Button btnBack;
    
    private MonHocRepository monHocRepository;
    private MonHocAdapter adapter;
    private List<MonHoc> monHocList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monhoc_list);

        initViews();
        setupListeners();
        
        monHocRepository = new MonHocRepository();
        monHocList = new ArrayList<>();
        
        // Setup RecyclerView
        adapter = new MonHocAdapter(monHocList);
        adapter.setOnItemClickListener(monHoc -> {
            // Chuyển sang màn hình cập nhật
            openUpdateActivity(monHoc);
        });
        recyclerViewMonHoc.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMonHoc.setAdapter(adapter);
        recyclerViewMonHoc.setHasFixedSize(false);
        recyclerViewMonHoc.setNestedScrollingEnabled(true);

        // Load danh sách môn học
        loadMonHocList();
    }

    private void initViews() {
        recyclerViewMonHoc = findViewById(R.id.recyclerViewMonHoc);
        fabAdd = findViewById(R.id.fabAdd);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> {
            // Chuyển sang màn hình thêm mới
            android.content.Intent intent = new android.content.Intent(this, AddMonHocActivity.class);
            startActivity(intent);
        });
        
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadMonHocList() {
        android.util.Log.d("MonHocListActivity", "Loading MonHoc list...");
        Toast.makeText(this, "Đang tải danh sách môn học...", Toast.LENGTH_SHORT).show();
        
        monHocRepository.getAllMonHoc(new MonHocRepository.MonHocCallback() {
            @Override
            public void onSuccess(List<MonHoc> monHocs) {
                runOnUiThread(() -> {
                    android.util.Log.d("MonHocListActivity", "Received " + monHocs.size() + " MonHoc");
                    monHocList.clear();
                    if (monHocs != null && !monHocs.isEmpty()) {
                        monHocList.addAll(monHocs);
                        adapter.updateList(monHocList);
                        Toast.makeText(MonHocListActivity.this, "Đã tải " + monHocs.size() + " môn học", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.updateList(monHocList);
                        Toast.makeText(MonHocListActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    android.util.Log.e("MonHocListActivity", "Error loading: " + error);
                    Toast.makeText(MonHocListActivity.this, "Lỗi tải danh sách môn học: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void openUpdateActivity(MonHoc monHoc) {
        android.content.Intent intent = new android.content.Intent(this, UpdateMonHocActivity.class);
        intent.putExtra("MON_HOC", monHoc);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách khi quay lại màn hình
        loadMonHocList();
    }
}

