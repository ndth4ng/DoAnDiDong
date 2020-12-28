package com.example.myapplication.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.Fragment.Home;
import com.example.myapplication.Fragment.Search;
import com.example.myapplication.Fragment.Wishlist;
import com.example.myapplication.Fragment.Cart;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Home();
            case 1:
                return new Wishlist();
            case 2:
                return new Search();
            case 3:
                return new Cart();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
