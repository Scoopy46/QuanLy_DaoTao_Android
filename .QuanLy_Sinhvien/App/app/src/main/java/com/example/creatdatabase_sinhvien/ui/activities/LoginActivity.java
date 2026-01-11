package com.example.creatdatabase_sinhvien.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Users;
import com.example.creatdatabase_sinhvien.repositories.UsersRepository;

/**
 * Activity đăng nhập
 */
public class LoginActivity extends AppCompatActivity {
    private EditText edtUserName, edtPassword;
    private Button btnLogin;
    private TextView txtRegister;
    
    private UsersRepository usersRepository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
        
        usersRepository = new UsersRepository();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
    }

    private void initViews() {
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
        
        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin() {
        String userName = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);
        Toast.makeText(this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();

        usersRepository.login(userName, password, new UsersRepository.LoginCallback() {
            @Override
            public void onSuccess(Users user) {
                runOnUiThread(() -> {
                    // Lưu thông tin đăng nhập
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", user.getUserName());
                    editor.putInt("userID", user.getUserID() != null ? user.getUserID() : 0);
                    editor.putString("fullName", user.getFullName());
                    editor.putString("type", user.getType());
                    
                    // Token sẽ được lưu trong UsersRepository nếu có
                    // Nếu server trả token trong body, có thể thêm vào đây
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    navigateToHome();
                    btnLogin.setEnabled(true);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + error, Toast.LENGTH_LONG).show();
                    btnLogin.setEnabled(true);
                });
            }
        });
    }

    private void navigateToHome() {
        // Quay về HomeActivity và refresh UI
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}

