package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Users;
import com.example.creatdatabase_sinhvien.repositories.UsersRepository;

/**
 * Activity để cập nhật thông tin người dùng
 */
public class UpdateUserActivity extends AppCompatActivity {
    private EditText edtUserID, edtUserName, edtPassword, edtFullName, edtMaGV;
    private Spinner spinnerType;
    private Button btnHuy, btnLuu, btnXoa;
    
    private UsersRepository usersRepository;
    private Users currentUser;
    private String selectedType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        // Lấy dữ liệu user từ Intent
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            currentUser = getIntent().getSerializableExtra("USER", Users.class);
        } else {
            currentUser = (Users) getIntent().getSerializableExtra("USER");
        }
        
        if (currentUser == null) {
            Toast.makeText(this, "Không có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        setupTypeSpinner();
        loadUserData();
        
        usersRepository = new UsersRepository();
    }

    private void initViews() {
        edtUserID = findViewById(R.id.edtUserID);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullName = findViewById(R.id.edtFullName);
        edtMaGV = findViewById(R.id.edtMaGV);
        spinnerType = findViewById(R.id.spinnerType);
        btnHuy = findViewById(R.id.btnHuy);
        btnLuu = findViewById(R.id.btnLuu);
        btnXoa = findViewById(R.id.btnXoa);
        
        // UserID chỉ xem, không cho sửa
        edtUserID.setEnabled(false);
        edtUserID.setFocusable(false);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> updateUser());
        btnXoa.setOnClickListener(v -> showDeleteConfirmation());
        
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedType = parent.getItemAtPosition(position).toString();
                } else {
                    selectedType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedType = "";
            }
        });
    }

    private void setupTypeSpinner() {
        String[] types = {"-- Chọn loại tài khoản --", "Admin", "GiaoVien", "SinhVien"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    private void loadUserData() {
        if (currentUser != null) {
            edtUserID.setText(currentUser.getUserID() != null ? currentUser.getUserID().toString() : "");
            edtUserName.setText(currentUser.getUserName());
            edtPassword.setText(currentUser.getPassword());
            edtFullName.setText(currentUser.getFullName());
            edtMaGV.setText(currentUser.getMaGV() != null ? currentUser.getMaGV() : "");
            selectedType = currentUser.getType() != null ? currentUser.getType() : "";
            
            // Chọn type trong spinner
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerType.getAdapter();
            if (adapter != null && !selectedType.isEmpty()) {
                int position = adapter.getPosition(selectedType);
                if (position >= 0) {
                    spinnerType.setSelection(position);
                }
            }
        }
    }

    private void updateUser() {
        String userName = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String maGV = edtMaGV.getText().toString().trim();

        if (userName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập họ và tên", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedType.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn loại tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser.getUserID() == null) {
            Toast.makeText(this, "Không thể cập nhật: ID không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Users user = new Users();
        user.setUserID(currentUser.getUserID());
        user.setUserName(userName);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setType(selectedType);
        user.setMaGV(maGV.isEmpty() ? null : maGV);

        btnLuu.setEnabled(false);
        Toast.makeText(this, "Đang cập nhật...", Toast.LENGTH_SHORT).show();

        usersRepository.updateUser(currentUser.getUserID(), user, new UsersRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateUserActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                    btnLuu.setEnabled(true);
                });
            }
        });
    }

    private void showDeleteConfirmation() {
        String userName = currentUser.getUserName() != null ? currentUser.getUserName() : "";
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa người dùng " + userName + " không?")
            .setPositiveButton("Đồng ý", (dialog, which) -> deleteUser())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteUser() {
        if (currentUser.getUserID() == null) {
            Toast.makeText(this, "Không thể xóa: ID không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        
        usersRepository.deleteUser(currentUser.getUserID(), new UsersRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateUserActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateUserActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}

