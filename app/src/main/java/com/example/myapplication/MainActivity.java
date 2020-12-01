package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ImageButton user;
    ImageButton menu_left;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();
        }

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), User.class));
                finish();
            }
        });


       actionToolBar();
    }
    void actionToolBar(){
        menu_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }



    void AnhXa(){
        user = findViewById(R.id.user);
        drawerLayout = findViewById(R.id.drawableLayout);
        menu_left = findViewById(R.id.menu_left);
    }
    private NavigationView.OnNavigationItemSelectedListener navigation =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.aoNam: {
                            Intent intent = new Intent(MainActivity.this, Categories.class);
                            startActivity(intent);
                            break;
                        }
                        case R.id.quanNam: {
                            Intent intent = new Intent(MainActivity.this, Categories.class);
                            startActivity(intent);
                            break;
                        }
                        case R.id.aoNu: {
                            Intent intent = new Intent(MainActivity.this, Categories.class);
                            startActivity(intent);
                            break;
                        }
                        case R.id.quanNu: {
                            Intent intent = new Intent(MainActivity.this, Categories.class);
                            startActivity(intent);
                            break;
                        }
                        case R.id.vay: {
                            Intent intent = new Intent(MainActivity.this, Categories.class);
                            startActivity(intent);
                            break;
                        }
                    }
                    return true;
                }
            };
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.home:
                            selectedFragment = new Home();
                            break;
                        case R.id.wishlist:
                            selectedFragment = new Wishlist();
                            Toast.makeText(MainActivity.this, "Nhấn giữ sản phẩm để xóa", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.search:
                            selectedFragment = new Search();
                            break;
                        case R.id.cart:
                            selectedFragment = new Cart();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}