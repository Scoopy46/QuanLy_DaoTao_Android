package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.adapters.SinhVienAdapter;
import com.example.creatdatabase_sinhvien.models.SinhVien;
import com.example.creatdatabase_sinhvien.repositories.SinhVienRepository;
import com.example.creatdatabase_sinhvien.ui.dialogs.ImagePickerDialog;
import com.example.creatdatabase_sinhvien.utils.ImageHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity chính để quản lý sinh viên
 */
public class MainActivity extends AppCompatActivity {
    private EditText edtMaSV, edtHoTen, edtNamSinh, edtLop, edtTimKiem;
    private ImageView imgAvatar;
    private Button btnChonAnh, btnThem, btnLuu, btnHuy, btnXoa, btnThoat, btnNhapDiem;
    private ListView listViewSV;
    
    private SinhVienRepository repository;
    private SinhVienAdapter adapter;
    private List<SinhVien> sinhVienList;
    private List<SinhVien> filteredList;
    private SinhVien selectedSinhVien;
    private String currentImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();
        
        repository = new SinhVienRepository();
        sinhVienList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new SinhVienAdapter(this, filteredList);
        listViewSV.setAdapter(adapter);

        listViewSV.setScrollingCacheEnabled(true);
        listViewSV.setSmoothScrollbarEnabled(true);
        listViewSV.setFastScrollEnabled(true);

        ImageHelper.copyImagesFromAssets(this);

        String imageDirPath = ImageHelper.getImageDirectoryPath(this);
        android.util.Log.d("MainActivity", "Image directory: " + imageDirPath);
        Toast.makeText(this, "Thư mục ảnh: " + imageDirPath, Toast.LENGTH_LONG).show();

        loadSinhVienList();
    }
    
    private void initViews() {
        edtMaSV = findViewById(R.id.edtMaSV);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtLop = findViewById(R.id.edtLop);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        btnThem = findViewById(R.id.btnThem);
        btnLuu = findViewById(R.id.btnLuu);
        btnHuy = findViewById(R.id.btnHuy);
        btnXoa = findViewById(R.id.btnXoa);
        btnThoat = findViewById(R.id.btnThoat);
        btnNhapDiem = findViewById(R.id.btnNhapDiem);
        listViewSV = findViewById(R.id.listViewSV);
    }

    private void setupListeners() {
        btnThem.setOnClickListener(v -> handleThem());
        btnLuu.setOnClickListener(v -> handleLuu());
        btnHuy.setOnClickListener(v -> handleHuy());
        btnXoa.setOnClickListener(v -> handleXoa());
        btnThoat.setOnClickListener(v -> finish());
        btnNhapDiem.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, NhapDiemActivity.class);
            startActivity(intent);
        });
        btnChonAnh.setOnClickListener(v -> {
            ImagePickerDialog dialog = new ImagePickerDialog(this);
            dialog.setOnImageSelectedListener((imageName, imagePath) -> {
                currentImagePath = "/storage/DCIM/" + imageName;
                ImageHelper.loadImage(this, imgAvatar, imagePath);
                Toast.makeText(this, "Đã chọn ảnh: " + imageName, Toast.LENGTH_SHORT).show();
            });
            dialog.show();
        });

        listViewSV.setOnItemClickListener((parent, view, position, id) -> {
            selectedSinhVien = filteredList.get(position);
            fillForm(selectedSinhVien);
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterByClass();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void handleThem() {
        if (validateInput()) {
            SinhVien sv = getSinhVienFromForm();
            repository.createSinhVien(sv, new SinhVienRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadSinhVienList();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleLuu() {
        if (selectedSinhVien == null) {
            Toast.makeText(this, "Vui lòng chọn sinh viên để cập nhật", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (validateInput()) {
            SinhVien sv = getSinhVienFromForm();
            repository.updateSinhVien(selectedSinhVien.getMaSV(), sv, new SinhVienRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show();
                    clearForm();
                    selectedSinhVien = null;
                    loadSinhVienList();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleHuy() {
        clearForm();
        selectedSinhVien = null;
    }

    private void handleXoa() {
        if (selectedSinhVien == null) {
            Toast.makeText(this, "Vui lòng chọn sinh viên để xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        repository.deleteSinhVien(selectedSinhVien.getMaSV(), new SinhVienRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Xóa sinh viên thành công", Toast.LENGTH_SHORT).show();
                clearForm();
                selectedSinhVien = null;
                loadSinhVienList();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSinhVienList() {
        Toast.makeText(this, "Đang tải danh sách sinh viên...", Toast.LENGTH_SHORT).show();
        repository.getAllSinhVien(new SinhVienRepository.SinhVienCallback() {
            @Override
            public void onSuccess(List<SinhVien> sinhViens) {
                runOnUiThread(() -> {
                    sinhVienList = sinhViens != null ? sinhViens : new ArrayList<>();
                    android.util.Log.d("MainActivity", "Loaded " + sinhVienList.size() + " sinh viên");
                    if (sinhVienList.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Đã tải " + sinhVienList.size() + " sinh viên", Toast.LENGTH_SHORT).show();
                    }
                    filterByClass();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    android.util.Log.e("MainActivity", "Error loading: " + error);
                    sinhVienList = new ArrayList<>();
                    filterByClass();
                });
            }
        });
    }

    private void filterByClass() {
        String searchText = edtTimKiem.getText().toString().trim().toLowerCase();
        filteredList.clear();
        
        if (TextUtils.isEmpty(searchText)) {
            filteredList.addAll(sinhVienList);
        } else {
            for (SinhVien sv : sinhVienList) {
                if (sv.getLop() != null && sv.getLop().toLowerCase().contains(searchText)) {
                    filteredList.add(sv);
                }
            }
        }
        
        android.util.Log.d("MainActivity", "filterByClass - sinhVienList size: " + sinhVienList.size() + ", filteredList size: " + filteredList.size());

        runOnUiThread(() -> {
            adapter.updateList(filteredList);
            listViewSV.invalidateViews();
            listViewSV.requestLayout();
            android.util.Log.d("MainActivity", "Adapter updated, getCount: " + adapter.getCount());
        });
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(edtMaSV.getText().toString().trim())) {
            Toast.makeText(this, "Vui lòng nhập mã sinh viên", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtHoTen.getText().toString().trim())) {
            Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtNamSinh.getText().toString().trim())) {
            Toast.makeText(this, "Vui lòng nhập năm sinh", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(edtLop.getText().toString().trim())) {
            Toast.makeText(this, "Vui lòng nhập lớp", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private SinhVien getSinhVienFromForm() {
        String maSV = edtMaSV.getText().toString().trim();
        String hoTen = edtHoTen.getText().toString().trim();
        int namSinh = Integer.parseInt(edtNamSinh.getText().toString().trim());
        String lop = edtLop.getText().toString().trim();
        String anh = currentImagePath;
        
        return new SinhVien(maSV, hoTen, namSinh, lop, anh);
    }

    private void fillForm(SinhVien sv) {
        if (sv != null) {
            edtMaSV.setText(sv.getMaSV() != null ? sv.getMaSV() : "");
            edtHoTen.setText(sv.getHoTen() != null ? sv.getHoTen() : "");
            edtNamSinh.setText(String.valueOf(sv.getNamSinh()));
            edtLop.setText(sv.getLop() != null ? sv.getLop() : "");

            if (sv.getAnh() != null && !sv.getAnh().isEmpty()) {
                currentImagePath = sv.getAnh();
                String imageName = extractImageName(sv.getAnh());
                String imagePath = new java.io.File(getFilesDir(), "images/" + imageName).getAbsolutePath();
                ImageHelper.loadImage(this, imgAvatar, imagePath);
            } else {
                currentImagePath = "";
                imgAvatar.setImageResource(0);
            }
        }
    }

    private String extractImageName(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return "";
        }
        if (imagePath.contains("/")) {
            return imagePath.substring(imagePath.lastIndexOf("/") + 1);
        }
        return imagePath;
    }

    private void clearForm() {
        edtMaSV.setText("");
        edtHoTen.setText("");
        edtNamSinh.setText("");
        edtLop.setText("");
        currentImagePath = "";
        imgAvatar.setImageResource(0);
    }
}

