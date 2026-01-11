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
import com.example.creatdatabase_sinhvien.adapters.UsersAdapter;
import com.example.creatdatabase_sinhvien.models.Users;
import com.example.creatdatabase_sinhvien.repositories.UsersRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity hiển thị danh sách người dùng
 */
public class UsersListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private FloatingActionButton fabAdd;
    private Button btnBack;
    
    private UsersRepository usersRepository;
    private UsersAdapter adapter;
    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        initViews();
        setupListeners();
        
        usersRepository = new UsersRepository();
        usersList = new ArrayList<>();
        
        // Setup RecyclerView
        adapter = new UsersAdapter(usersList);
        adapter.setOnItemClickListener(user -> {
            // Chuyển sang màn hình cập nhật
            openUpdateActivity(user);
        });
        adapter.setOnItemLongClickListener(user -> {
            // Hiển thị dialog xác nhận xóa
            showDeleteConfirmation(user);
            return true;
        });
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(adapter);
        recyclerViewUsers.setHasFixedSize(false);
        recyclerViewUsers.setNestedScrollingEnabled(true);

        // Load danh sách users
        loadUsersList();
    }

    private void initViews() {
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        fabAdd = findViewById(R.id.fabAdd);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        fabAdd.setOnClickListener(v -> {
            // Chuyển sang màn hình thêm mới
            android.content.Intent intent = new android.content.Intent(this, AddUserActivity.class);
            startActivity(intent);
        });
        
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadUsersList() {
        android.util.Log.d("UsersListActivity", "Loading Users list...");
        Toast.makeText(this, "Đang tải danh sách người dùng...", Toast.LENGTH_SHORT).show();
        
        usersRepository.getAllUsers(new UsersRepository.UsersCallback() {
            @Override
            public void onSuccess(List<Users> users) {
                runOnUiThread(() -> {
                    android.util.Log.d("UsersListActivity", "Received " + users.size() + " Users");
                    usersList.clear();
                    if (users != null && !users.isEmpty()) {
                        usersList.addAll(users);
                        adapter.updateList(usersList);
                        Toast.makeText(UsersListActivity.this, "Đã tải " + users.size() + " người dùng", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.updateList(usersList);
                        Toast.makeText(UsersListActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    android.util.Log.e("UsersListActivity", "Error loading: " + error);
                    Toast.makeText(UsersListActivity.this, "Lỗi tải danh sách: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void openUpdateActivity(Users user) {
        android.content.Intent intent = new android.content.Intent(this, UpdateUserActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    private void showDeleteConfirmation(Users user) {
        String userName = user.getUserName() != null ? user.getUserName() : "";
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa người dùng " + userName + " không?")
            .setPositiveButton("Đồng ý", (dialog, which) -> deleteUser(user))
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void deleteUser(Users user) {
        if (user.getUserID() == null) {
            Toast.makeText(this, "Không thể xóa: ID không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        
        usersRepository.deleteUser(user.getUserID(), new UsersRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(UsersListActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadUsersList();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(UsersListActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách khi quay lại màn hình
        loadUsersList();
    }
}

