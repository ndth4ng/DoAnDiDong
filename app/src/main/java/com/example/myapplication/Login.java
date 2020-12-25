package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    FirebaseAuth fAuth;
    Button btnLogin;
    Button btnRegister;
    EditText edtEmail, edtPassword;
    TextView tvReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        AnhXa();

        FirebaseUser user = null;

        user = fAuth.getCurrentUser();

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

                /*if (password.length() < 8) {
                    edtPassword.setError("Mật khẩu phải từ 8 ký tự!");
                    return;
                }*/

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

        // Đăng ký

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        // Quên mật khẩu

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder resetDialog = new AlertDialog.Builder(view.getContext());
                resetDialog.setTitle("Quên mật khẩu?");
                resetDialog.setMessage("Vui lòng nhập email để tìm kiếm tài khoản.");
                resetDialog.setView(resetMail);

                resetDialog.setPositiveButton("Tìm kiếm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Đường dẫn lấy lại mật khẩu đã được gửi đến Email của bạn.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Email này không tồn tại.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                resetDialog.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Thoát dialog
                    }
                });

                resetDialog.create().show();
            }
        });
    }

    protected void AnhXa() {
        fAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tvReset = findViewById(R.id.tvReset);
    }
}