package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button btnRegister;
    TextView tvLogin;
    EditText edtName,edtPassword,edtComfirm,edtEmail,edtAddress,edtPhone;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        final FirebaseUser user = fAuth.getCurrentUser();

        if (user == null) {
            return;
        } else {
            if (!user.isAnonymous()) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirm = edtComfirm.getText().toString().trim();
                final String name = edtName.getText().toString();
                final String address = edtAddress.getText().toString();
                final String phone = edtPhone.getText().toString();

                if (TextUtils.isEmpty((email))) {
                    edtEmail.setError("Chọn một địa chỉ Email");
                    return;
                }

                if (TextUtils.isEmpty((password))) {
                    edtPassword.setError("Mật khẩu không được để trống!");
                    return;
                }

                if (TextUtils.isEmpty((confirm))) {
                    edtPassword.setError("Mật khẩu không được để trống!");
                    return;
                }

                if (!confirm.equals(password)) {
                    edtPassword.setError("Mật khẩu không giống nhau!");
                }

                if(password.length() < 8) {
                    edtPassword.setError("Mật khẩu phải từ 8 ký tự!");
                    return;
                }

                // Đăng ký trên Firebase
                if (user == null) {
                    NormalRegister(email,password,name,address,phone);
                } else {
                    ConvertRegister(email,password,name,address,phone);
                }

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    private void ConvertRegister(final String email, String password, final String name, final String address, final String phone) {
        // Đăng ký trên Firebase
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        fAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "linkWithCredential:success");

                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);

                            Map<String, Object> user = new HashMap<>();
                            user.put("Tên", name);
                            user.put("Email", email);
                            user.put("Địa chỉ", address);
                            user.put("Số điện thoại", phone);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "Tạo profile thành công cho " + userID);
                                }
                            });

                            setResult(Register.RESULT_CANCELED);
                            Toast.makeText(Register.this,"Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();

                        } else {
                            //Log.d("TAG", "linkWithCredential:failure", task.getException());
                            edtEmail.setError("Email đã tồn tại hoặc không đúng định dạng");
                        }
                    }
                });
    }

    private void NormalRegister(final String email, String password, final String name, final String address, final String phone) {
        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);

                    Map<String, Object> user = new HashMap<>();
                    user.put("Tên",name);
                    user.put("Email",email);
                    user.put("Địa chỉ",address);
                    user.put("Số điện thoại",phone);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "Tạo profile thành công cho " + userID);
                        }
                    });

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                else {
                    edtEmail.setError("Email đã tồn tại hoặc không đúng định dạng");
                }
            }
        });
    }

    protected void AnhXa() {
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.login_register);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        edtComfirm = findViewById(R.id.edtConfirm);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
    }
}