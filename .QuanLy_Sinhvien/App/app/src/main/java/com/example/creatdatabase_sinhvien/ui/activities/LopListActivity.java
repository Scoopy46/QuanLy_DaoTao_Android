package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.adapters.LopAdapter;
import com.example.creatdatabase_sinhvien.models.Lop;
import com.example.creatdatabase_sinhvien.repositories.LopRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity hiển thị danh sách lớp học
 */
public class LopListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewLop;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabBack;
    
    private LopRepository lopRepository;
    private LopAdapter adapter;
    private List<Lop> lopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lop_list);

        initViews();
        setupListeners();
        
        lopRepository = new LopRepository();
        lopList = new ArrayList<>();
        
        // Setup RecyclerView
        adapter = new LopAdapter(this, lopList);
        adapter.setOnItemClickListener(lop -> {
            // Chuyển sang màn hình cập nhật
            openUpdateActivity(lop);
        });
        adapter.setOnItemLongClickListener(lop -> {
            // Hiển thị dialog xác nhận xóa
            showDeleteConfirmation(lop);
            return true;
        });
        recyclerViewLop.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLop.setAdapter(adapter);
        recyclerViewLop.setHasFixedSize(false);
        recyclerViewLop.setNestedScrollingEnabled(true);

        // Load danh sách lớp
        loadLopList();
    }

    private void initViews() {
        recyclerViewLop = findViewById(R.id.recyclerViewLop);
        fabAdd = findViewById(R.id.fabAdd);
        fabBack = findViewById(R.id.fabBack);
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> {
            // Chuyển sang màn hình thêm mới
            android.content.Intent intent = new android.content.Intent(this, AddLopActivity.class);
            startActivity(intent);
        });
        
        fabBack.setOnClickListener(v -> finish());
    }

    private void loadLopList() {
        android.util.Log.d("LopListActivity", "Loading Lop list...");
        Toast.makeText(this, "Đang tải danh sách lớp...", Toast.LENGTH_SHORT).show();
        
        lopRepository.getAllLop(new LopRepository.LopCallback() {
            @Override
            public void onSuccess(List<Lop> lops) {
                runOnUiThread(() -> {
                    android.util.Log.d("LopListActivity", "Received " + lops.size() + " Lop");
                    lopList.clear();
                    if (lops != null && !lops.isEmpty()) {
                        lopList.addAll(lops);
                        adapter.updateList(lopList);
                        Toast.makeText(LopListActivity.this, "Đã tải " + lops.size() + " lớp", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.updateList(lopList);
                        Toast.makeText(LopListActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    android.util.Log.e("LopListActivity", "Error loading: " + error);
                    Toast.makeText(LopListActivity.this, "Lỗi tải danh sách lớp: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void openUpdateActivity(Lop lop) {
        android.content.Intent intent = new android.content.Intent(this, UpdateLopActivity.class);
        intent.putExtra("LOP", lop);
        startActivity(intent);
    }

    private void showDeleteConfirmation(Lop lop) {
        String lopName = lop.getTenLop() != null ? lop.getTenLop() : lop.getMaLop();
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa lớp " + lopName + " không?")
            .setPositiveButton("Đồng ý", (dialog, which) -> deleteLop(lop))
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteLop(Lop lop) {
        lopRepository.deleteLop(lop.getMaLop(), new LopRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(LopListActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadLopList();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(LopListActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách khi quay lại màn hình
        loadLopList();
    }
}

