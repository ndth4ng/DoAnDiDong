package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;

public class Wishlist extends Fragment {

    ListView listView;
    ArrayList<Furniture> furnitureArrayList = new ArrayList<>();
    FurnitureAdapter furnitureAdapter;
    View view;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wishlist, container, false);
        listView = view.findViewById(R.id.listView);
        furnitureArrayList.add(new Furniture("Zinus Jocelyn","Lorem ipsum dolor sit amet, consectetue, purus lectus malesuada libero.", 299, R.drawable.ao));
        furnitureArrayList.add(new Furniture("Zinus Jocelyn","Lorem ipsum dolor sit amet, consectetue, purus lectus malesuada libero.", 300, R.drawable.ao));
        furnitureArrayList.add(new Furniture("Zinus Jocelyn","Lorem ipsum dolor sit amet, consectetue, purus lectus malesuada libero.", 300, R.drawable.ao));
        furnitureArrayList.add(new Furniture("Zinus Jocelyn","Lorem ipsum dolor sit amet, consectetue, purus lectus malesuada libero.", 300, R.drawable.ao));
        furnitureArrayList.add(new Furniture("Zinus Jocelyn","Lorem ipsum dolor sit amet, consectetue, purus lectus malesuada libero.", 303, R.drawable.ao));
        furnitureArrayList.add(new Furniture("Zinus Jocelyn","Lorem ipsum dolor sit amet, consectetue, purus lectus malesuada libero.", 301, R.drawable.ao));
        furnitureAdapter = new FurnitureAdapter(getActivity(), R.layout.row_wishlist, furnitureArrayList);
        listView.setAdapter(furnitureAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                furnitureArrayList.remove(position);
                listView.setAdapter(furnitureAdapter);
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), details.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);

            }
        });
        return view;

    }
}