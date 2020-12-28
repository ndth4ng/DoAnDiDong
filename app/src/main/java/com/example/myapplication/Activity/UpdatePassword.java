package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {

    TextView tvOldPass, tvNewPass, tvConfirm;
    ImageView close;
    Button btnUpdate;
    FirebaseUser user;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvOldPass.getText().toString().isEmpty() || tvNewPass.getText().toString().isEmpty() || tvConfirm.getText().toString().isEmpty()) {
                    Toast.makeText(UpdatePassword.this,"Vui lòng điền tất cả các dòng.",Toast.LENGTH_SHORT).show();
                } else {
                    if (tvNewPass.getText().toString().equals(tvConfirm.getText().toString())) {
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), tvOldPass.getText().toString());
                        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                user.updatePassword(tvNewPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UpdatePassword.this,"Thay đổi mật khẩu thành công.",Toast.LENGTH_SHORT).show();
                                        //startActivity(new Intent(getApplicationContext(),User.class));
                                        finish();;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UpdatePassword.this,"Thay đổi mật khẩu không thành công.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdatePassword.this,"Mật khẩu hiện tại không đúng.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(UpdatePassword.this,"Mật khẩu xác nhận và mật khẩu mới không giống nhau.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), User.class));
        finish();
    }

    protected void AnhXa() {
        tvOldPass = findViewById(R.id.tvOldPassword);
        tvNewPass = findViewById(R.id.tvNewPassword);
        tvConfirm = findViewById(R.id.tvConfirmPassword);

        btnUpdate = findViewById(R.id.btnUpdate);
        close = findViewById(R.id.close);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_out_down);
    }
}