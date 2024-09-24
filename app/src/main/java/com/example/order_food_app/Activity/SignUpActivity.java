package com.example.order_food_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.order_food_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText signupEmail, signupPassword, signupFullName;
    private Button signupButton;
    private TextView loginRedirectText;
    private DatabaseReference usersReference;
    private DatabaseReference currentIdReference; // For ID management (without Authentication)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singn_up);

        initializeViews();
        setupFirebaseReferences();
        setupListeners();
    }

    private void initializeViews() {
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupFullName = findViewById(R.id.signup_fullname);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
    }

    private void setupFirebaseReferences() {
        usersReference = FirebaseDatabase.getInstance().getReference("Users");
        currentIdReference = FirebaseDatabase.getInstance().getReference("currentId");
    }

    private void setupListeners() {
        signupButton.setOnClickListener(view -> {
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            String fullName = signupFullName.getText().toString().trim();

            if (validateInputs(email, password)) {
                createUser(fullName, email, password);
            }
        });

        loginRedirectText.setOnClickListener(view ->
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class))
        );
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            signupEmail.setError("Không được để trống Email");
            return false;
        }
        if (password.isEmpty()) {
            signupPassword.setError("Không được để trống mật khẩu");
            return false;
        }
        return true;
    }

    private void createUser(String fullName, String email, String password) {
        currentIdReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                int currentId = (currentData.getValue() == null) ? 1 : currentData.getValue(Integer.class) + 1;
                currentData.setValue(currentId);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                if (committed) {
                    int newId = currentData.getValue(Integer.class);
                    User user = new User(newId, fullName, email, password);
                    saveUserToDatabase(user);
                } else {
                    if (error != null) {
                        Toast.makeText(SignUpActivity.this, "Lỗi tạo ID: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Lỗi khi tạo ID người dùng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void saveUserToDatabase(User user) {
        usersReference.child(String.valueOf(user.id)).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(SignUpActivity.this, "Lưu thông tin thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class User {
        public int id;
        public String fullName;
        public String email;
        public String password;

        public User(int id, String fullName, String email, String password) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.password = password;
        }
    }
}