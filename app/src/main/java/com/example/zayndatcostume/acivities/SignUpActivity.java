package com.example.zayndatcostume.acivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zayndatcostume.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtSignupUsername, edtSignupPassword, edtSignupRetypePassword;
    private Button btnSignup;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        initUi();
        initListener();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initUi(){
        edtSignupUsername = findViewById(R.id.signup_username_edt);
        edtSignupPassword = findViewById(R.id.signup_password_edt);
        edtSignupRetypePassword = findViewById(R.id.signup_retype_password_edt);
        btnSignup = findViewById(R.id.signup_button);
        progressDialog = new ProgressDialog(SignUpActivity.this);
    }

    private void initListener() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickSignUp();
            }

            private void OnClickSignUp() {
                String email = edtSignupUsername.getText().toString().trim();
                String password = edtSignupPassword.getText().toString().trim();
                String retypePassword = edtSignupRetypePassword.getText().toString().trim();

                // Kiểm tra nếu email hoặc password bị trống
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(retypePassword)) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra nếu mật khẩu và mật khẩu nhập lại không giống nhau
                if (!password.equals(retypePassword)) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    // Đăng ký thành công, chuyển đến MainActivity
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                } else {
                                    // Đăng ký thất bại, hiển thị thông báo lỗi
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
