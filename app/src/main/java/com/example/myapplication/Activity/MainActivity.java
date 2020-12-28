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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
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


    private static final int REQUEST_CODE_BADGE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        actionToolBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();
        }

        layoutParams = (CoordinatorLayout.LayoutParams) bottomNav.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehaviour());

        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        navigationView.setNavigationItemSelectedListener(navigation);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        getBadge();

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), User.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
                        layoutParams.setBehavior(new BottomNavigationBehaviour());
                    }
                        break;
                    case 1: {
                        bottomNav.getMenu().findItem(R.id.wishlist).setChecked(true);
                        layoutParams.setBehavior(new BottomNavigationBehaviour());
                    }
                        break;
                    case 2: {
                        bottomNav.getMenu().findItem(R.id.search).setChecked(true);
                        layoutParams.setBehavior(new BottomNavigationBehaviour());
                    }
                        break;
                    case 3: {
                        bottomNav.getMenu().findItem(R.id.cart).setChecked(true);
                        layoutParams.setBehavior(null);
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
        String userID = fAuth.getCurrentUser().getUid();
        CollectionReference collectRef = fStore.collection("users").document(userID).collection("Cart");
        collectRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int cartSize = task.getResult().size();
                    BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.cart);
                    badge.setVisible(true);

                    if (cartSize != 0){
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
                            startActivityForResult(intent,REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        }
                        case R.id.longshirt: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "longshirt");
                            startActivityForResult(intent,REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        }
                        case R.id.jacket: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "jacket");
                            startActivityForResult(intent,REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        }
                        case R.id.somi: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "somi");
                            startActivityForResult(intent,REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        }
                        case R.id.shortpant: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "shortpant");
                            startActivityForResult(intent,REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        }
                        case R.id.longpant: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "longpant");
                            startActivityForResult(intent,REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        }
                        case R.id.hat: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "hat");
                            startActivityForResult(intent,REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        }
                        case R.id.socks: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "socks");
                            startActivityForResult(intent,REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            break;
                        }
                        case R.id.belt: {
                            Intent intent = new Intent(getApplicationContext(), Categories.class);
                            intent.putExtra("cate", "belt");
                            startActivityForResult(intent,REQUEST_CODE_BADGE);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
    }

    public void setFragment(int itemID) {
        viewPager.setCurrentItem(itemID);
    }
}