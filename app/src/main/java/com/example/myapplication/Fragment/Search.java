package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.R;

public class Search extends Fragment {
    View view;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);
        ((MainActivity)getActivity()).getBadge();

        return view;


    }
}