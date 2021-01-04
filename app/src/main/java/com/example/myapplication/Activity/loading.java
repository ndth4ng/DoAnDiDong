package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class loading extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if (!isConnected(this)) {
            showCustomDialog();
        } else {

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();

            currentUser = fAuth.getCurrentUser();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (currentUser == null) {
                        fAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "signInAnonymously:success");
                                    currentUser = fAuth.getCurrentUser();
                                    Log.d("TAG", "signInAnonymously: " + currentUser.getUid());
                                } else {
                                    Log.d("TAG", "signInAnonymously:fail");
                                }
                            }
                        });
                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }, 1000); // 1000ms delay
        }
    }

    private boolean isConnected(loading loading) {
        ConnectivityManager connectivityManager = (ConnectivityManager) loading.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiCon != null && wifiCon.isConnected()) || (mobileCon != null & mobileCon.isConnected())) {
            return true;
        } else
            return false;
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(loading.this);
        builder.setMessage("Thiết bị chưa kết nối Internet. Vui lòng kết nối Internet sau đó khởi động tại ứng dụng.")
                .setCancelable(false)
                .setPositiveButton("Kết nối", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}