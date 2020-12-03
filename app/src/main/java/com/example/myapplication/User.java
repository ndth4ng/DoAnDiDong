package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class User extends AppCompatActivity {

    ImageView back;
    Button btnLogOut;
    TextView history, tvName, tvEmail, tvAddress, tvPhone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        //final FirebaseUser user = fAuth.getCurrentUser();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    tvName.setText(documentSnapshot.getString("Tên"));
                    tvEmail.setText(documentSnapshot.getString("Email"));
                    tvAddress.setText(documentSnapshot.getString("Địa chỉ"));
                    tvPhone.setText(documentSnapshot.getString("Số điện thoại"));
                } else {
                    Log.d("tag", " onEvent: Document do not exist.");
                }
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User.this, History.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

    protected void AnhXa() {
        back = findViewById(R.id.back);
        btnLogOut = findViewById(R.id.logout);
        history = findViewById(R.id.RHistoryy);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);

        Toolbar toolbar = findViewById(R.id.acction_bar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeProfile:
                // Cập nhật thông tin
                //startActivity(new Intent(getApplicationContext(),UpdateProfile.class));
                Intent i = new Intent(getApplicationContext(), UpdateProfile.class);
                i.putExtra("email", tvEmail.getText());
                i.putExtra("name", tvName.getText());
                i.putExtra("address", tvAddress.getText());
                i.putExtra("phone", tvPhone.getText());
                startActivity(i);
                finish();
                break;
            case R.id.changePassword:
                // Cập nhật mật khẩu
                startActivity(new Intent(getApplicationContext(), UpdatePassword.class));
                finish();
                break;
            default:
                // ...
        }
        return super.onOptionsItemSelected(item);
    }
}