package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    TextView tvName, tvAddress, tvPhone;
    ImageView close;
    Button btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        user = fAuth.getCurrentUser();

        Intent data = getIntent();

        final String userEmail = data.getStringExtra("email");
        final String name = data.getStringExtra("name");
        String address = data.getStringExtra("address");
        String phone = data.getStringExtra("phone");

        tvName.setText(name);
        tvAddress.setText(address);
        tvPhone.setText(phone);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvName.getText().toString().isEmpty() || tvAddress.getText().toString().isEmpty() || tvPhone.getText().toString().isEmpty()) {
                    Toast.makeText(UpdateProfile.this, "Thiếu thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference documentReference = fStore.collection("users").document(user.getUid());
                Map<String,Object> edited = new HashMap<>();
                edited.put("Tên",tvName.getText().toString());
                edited.put("Địa chỉ",tvAddress.getText().toString());
                edited.put("Số điện thoại",tvPhone.getText().toString());
                documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateProfile.this,"Cập nhật thông tin thành công.",Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), User.class));
        finish();
    }

    protected void AnhXa() {
        close = findViewById(R.id.close);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_out_down);
    }
}