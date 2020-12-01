package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class User extends AppCompatActivity {

    ImageView back;
    ImageView setting;
    Button btnLogOut;
    RelativeLayout history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        AnhXa();

        /*setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(User.this, UpdateInformation.class);
                        startActivity(intent);
                }
        });*/

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
    }

    protected void AnhXa() {
        back = findViewById(R.id.back);
        setting = findViewById(R.id.setting);
        btnLogOut = findViewById(R.id.logout);
        history = findViewById(R.id.Rhistory);
    }

     public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}