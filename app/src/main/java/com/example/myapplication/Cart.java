package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Cart extends Fragment {
    ListView listView;
    ArrayList<Furniture> furnitureArrayList = new ArrayList<>();
    //FurnitureAdapter furnitureAdapter;
    View viewC;

    Button btnPay;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewC = inflater.inflate(R.layout.activity_cart, container, false);



        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Pay.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);

            }
        });

        return viewC;
    }
}