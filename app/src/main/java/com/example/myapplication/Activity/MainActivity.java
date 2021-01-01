package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapter.ViewPagerAdapter;
import com.example.myapplication.Fragment.Cart;
import com.example.myapplication.Fragment.Home;
import com.example.myapplication.Fragment.Search;
import com.example.myapplication.Fragment.Wishlist;
import com.example.myapplication.Helper.BottomNavigationBehaviour;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    ImageButton user;
    ImageButton menu_left;
    DrawerLayout drawerLayout;
    CoordinatorLayout rootLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNav;
    ViewPager viewPager;

    CoordinatorLayout.LayoutParams layoutParams;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    ViewPagerAdapter viewPagerAdapter;

    FirebaseUser currentUser;

    private static final int REQUEST_CODE_BADGE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        currentUser = fAuth.getCurrentUser();

        if (currentUser == null) {
            fAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInAnonymously:success");
                        currentUser = fAuth.getCurrentUser();
                        Log.d("TAG", "signInAnonymously: "+currentUser.getUid());
                    } else {
                        Log.d("TAG", "signInAnonymously:fail");
                    }
                }
            });
        }

        AnhXa();

        actionToolBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();
        }
        //layoutParams = (CoordinatorLayout.LayoutParams) bottomNav.getLayoutParams();
        //layoutParams.setBehavior(new BottomNavigationBehaviour());

        //drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        navigationView.setNavigationItemSelectedListener(navigation);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        //getBadge();

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    if (!currentUser.isAnonymous()) {
                        startActivityForResult(new Intent(getApplicationContext(), User.class), 321);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        startActivityForResult(new Intent(getApplicationContext(), Login.class), 111);
                    }
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        bottomNav.getMenu().findItem(R.id.home).setChecked(true);
                    }
                    break;
                    case 1: {
                        bottomNav.getMenu().findItem(R.id.wishlist).setChecked(true);
                    }
                    break;
                    case 2: {
                        bottomNav.getMenu().findItem(R.id.search).setChecked(true);
                    }
                    break;
                    case 3: {
                        bottomNav.getMenu().findItem(R.id.cart).setChecked(true);
                        //layoutParams.setBehavior(null);
                    }
                    break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void getBadge() {
        if (fAuth.getCurrentUser() != null) {
            String userID = fAuth.getCurrentUser().getUid();
            CollectionReference collectRef = fStore.collection("users").document(userID).collection("Cart");
            collectRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        int cartSize = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            cartSize += Integer.parseInt(document.get("amount").toString());
                        }
                        BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.cart);
                        badge.setVisible(true);

                        if (cartSize != 0) {
                            badge.setNumber(cartSize);
                        } else {
                            badge.setVisible(false);
                        }
                    }
                }
            });
        }
    }
    void actionToolBar() {
        menu_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Bấm lần nữa để thoát", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    void AnhXa() {
        user = findViewById(R.id.user);
        drawerLayout = findViewById(R.id.drawer_layout);
        menu_left = findViewById(R.id.menu_left);
        bottomNav = findViewById(R.id.bottom_nav);
        navigationView = findViewById(R.id.navigationView);

        rootLayout = findViewById(R.id.rootLayout);
        viewPager = findViewById(R.id.view_pager);
    }

    private NavigationView.OnNavigationItemSelectedListener navigation =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.tshirt: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "tshirt");
                            startActivityForResult(intent, REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        }
                        case R.id.longshirt: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "longshirt");
                            startActivityForResult(intent, REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        }
                        case R.id.jacket: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "jacket");
                            startActivityForResult(intent, REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        }
                        case R.id.somi: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "somi");
                            startActivityForResult(intent, REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        }
                        case R.id.shortpant: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "shortpant");
                            startActivityForResult(intent, REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        }
                        case R.id.longpant: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "longpant");
                            startActivityForResult(intent, REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        }
                        case R.id.hat: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "hat");
                            startActivityForResult(intent, REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        }
                        case R.id.socks: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "socks");
                            startActivityForResult(intent, REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        }
                        case R.id.belt: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "belt");
                            startActivityForResult(intent, REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                    switch (item.getItemId()) {
                        case R.id.home:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.wishlist:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.search:
                            viewPager.setCurrentItem(2);
                            break;
                        case R.id.cart:
                            viewPager.setCurrentItem(3);
                            break;
                    }
                    return true;
                }
            };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_BADGE && resultCode == Activity.RESULT_OK) {
            getBadge();
        }

        if (requestCode == 321 && resultCode == Activity.RESULT_OK) {
            getBadge();
            setFragment(3);
        }

        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            finish();
        }
    }

    public void setFragment(int itemID) {
        viewPager.setCurrentItem(itemID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked");
    }
}