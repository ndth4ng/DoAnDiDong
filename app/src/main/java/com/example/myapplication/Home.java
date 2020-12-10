package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {
    View view;
    List<Item> itemList;
    RecyclerView recyclerView;
    ItemAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, container, false);

        recyclerView = view.findViewById(R.id.listItem);

        mAdapter = new ItemAdapter(getContext(), itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemList = new ArrayList<>();

        itemList.add(new Item("Áo",R.drawable.ao));
        itemList.add(new Item("Quần",R.drawable.quan));
        itemList.add(new Item("Vớ",R.drawable.vo));
        itemList.add(new Item("Quần",R.drawable.quan));
    }
}

