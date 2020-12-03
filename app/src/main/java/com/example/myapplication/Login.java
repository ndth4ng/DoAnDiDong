package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    FirebaseAuth fAuth;
    Button btnLogin;
    TextView tvRegister;
    EditText edtEmail, edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        AnhXa();

        FirebaseUser user = fAuth.getCurrentUser();

        if (user != null) {
            Toast.makeText(this,"Bạn đã đăng nhập từ trước.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        } else {
            Toast.makeText(this,"Hãy đăng nhập.",Toast.LENGTH_SHORT).show();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty((email))) {
                    edtEmail.setError("Email không được để trống!");
                    return;
                }

                if (TextUtils.isEmpty((password))) {
                    edtPassword.setError("Mật khẩu không được để trống!");
                    return;
                }

                if (password.length() < 8) {
                    edtPassword.setError("Mật khẩu phải từ 8 ký tự!");
                    return;
                }

                // Đăng nhập

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });
    }

    protected void AnhXa() {
        fAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.login_register);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
    }
}