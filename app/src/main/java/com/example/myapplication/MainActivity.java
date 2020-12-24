package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    ImageButton user;
    ImageButton menu_left;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNav;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        getBadge();

        actionToolBar();

        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        bottomNav = findViewById(R.id.bottom_nav);

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
                //finish();
            }
        });
    }

    public void getBadge() {
        String userID = fAuth.getCurrentUser().getUid();
        CollectionReference collectRef = fStore.collection("users").document(userID).collection("Cart");
        collectRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int cartSize = task.getResult().size();
                    BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.cart);
                    badge.setVisible(true);
                    if (badge == null) {
                        badge.setVisible(false);
                        badge.clearNumber();
                    } else if (cartSize != 0){
                        badge.setNumber(cartSize);
                    } else {
                        badge.setVisible(false);
                    }
                }
            }
        });
    }

    void actionToolBar() {
        menu_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    void AnhXa() {
        user = findViewById(R.id.user);
        drawerLayout = findViewById(R.id.drawer_layout);
        menu_left = findViewById(R.id.menu_left);
    }

    private NavigationView.OnNavigationItemSelectedListener navigation =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.tshirt: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "tshirt");
                            startActivityForResult(intent,300);
                            break;
                        }
                        case R.id.longshirt: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "longshirt");
                            startActivityForResult(intent,300);
                            break;
                        }
                        case R.id.jacket: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "jacket");
                            startActivityForResult(intent,300);
                            break;
                        }
                        case R.id.somi: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "somi");
                            startActivityForResult(intent,300);
                            break;
                        }
                        case R.id.shortpant: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "shortpant");
                            startActivityForResult(intent,300);
                            break;
                        }
                        case R.id.longpant: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "longpant");
                            startActivityForResult(intent,300);
                            break;
                        }
                        case R.id.hat: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "hat");
                            startActivityForResult(intent,300);
                            break;
                        }
                        case R.id.socks: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "socks");
                            startActivityForResult(intent,300);
                            break;
                        }
                        case R.id.belt: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "belt");
                            startActivityForResult(intent,300);
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
                            //Toast.makeText(MainActivity.this, "Nhấn giữ sản phẩm để xóa", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.search:
                            selectedFragment = new Search();
                            break;
                        case R.id.cart:
                            selectedFragment = new Cart();
                            break;
                    }

                    loadFragment(selectedFragment);

                    return true;
                }
            };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300)
            getBadge();
    }
}