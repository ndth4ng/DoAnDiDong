package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
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

        Log.d("tag","User ID day ne: " + userId);

        if (userId != null) {
            DocumentReference documentReference = fStore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    // nếu user không còn đăng nhập thì exception sẽ khác null nên sẽ không thực hiện
                    if (error == null) {
                        if (documentSnapshot.exists()) {
                            tvName.setText(documentSnapshot.getString("Tên"));
                            tvEmail.setText(documentSnapshot.getString("Email"));
                            tvAddress.setText(documentSnapshot.getString("Địa chỉ"));
                            tvPhone.setText(documentSnapshot.getString("Số điện thoại"));
                        } else {
                            Log.d("tag", " onEvent: Document do not exist.");
                        }
                    }
                }
            });
        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), History.class);
                startActivityForResult(intent, 321);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                Intent i = new Intent(getApplicationContext(), UpdateProfile.class);
                i.putExtra("email", tvEmail.getText());
                i.putExtra("name", tvName.getText());
                i.putExtra("address", tvAddress.getText());
                i.putExtra("phone", tvPhone.getText());
                startActivityForResult(i,0);
                break;
            case R.id.changePassword:
                // Cập nhật mật khẩu
                startActivity(new Intent(getApplicationContext(), UpdatePassword.class));
                break;
            default:
                // ...
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 321 && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            finish();
            startActivity(new Intent(getApplicationContext(),User.class));;
        }
    }
}