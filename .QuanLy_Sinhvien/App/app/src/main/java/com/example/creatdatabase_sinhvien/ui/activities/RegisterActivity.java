package com.example.creatdatabase_sinhvien.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Users;
import com.example.creatdatabase_sinhvien.repositories.UsersRepository;

/**
 * Activity đăng ký tài khoản
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText edtUserName, edtPassword, edtFullName, edtMaGV;
    private Spinner spinnerType;
    private Button btnHuy, btnDangKy;
    
    private UsersRepository usersRepository;
    private String selectedType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupListeners();
        setupTypeSpinner();
        
        usersRepository = new UsersRepository();
    }

    private void initViews() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullName = findViewById(R.id.edtFullName);
        edtMaGV = findViewById(R.id.edtMaGV);
        spinnerType = findViewById(R.id.spinnerType);
        btnHuy = findViewById(R.id.btnHuy);
        btnDangKy = findViewById(R.id.btnDangKy);
    }

    private void setupListeners() {
        btnHuy.setOnClickListener(v -> finish());
        btnDangKy.setOnClickListener(v -> performRegister());
        
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

    private void performRegister() {
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

        Users user = new Users();
        user.setUserName(userName);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setType(selectedType);
        user.setMaGV(maGV.isEmpty() ? null : maGV);
        user.setUserID(null); // Server sẽ tự tạo

        btnDangKy.setEnabled(false);
        Toast.makeText(this, "Đang đăng ký...", Toast.LENGTH_SHORT).show();

        usersRepository.createUser(user, new UsersRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + error, Toast.LENGTH_LONG).show();
                    btnDangKy.setEnabled(true);
                });
            }
        });
    }
}

