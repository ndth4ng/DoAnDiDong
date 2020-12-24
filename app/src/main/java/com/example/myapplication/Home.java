package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment implements ItemAdapter.OnItemListener {
    View view;
    List<Item> itemList;
    RecyclerView recyclerView;
    ItemAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, container, false);

        recyclerView = view.findViewById(R.id.listItem);

        mAdapter = new ItemAdapter(getContext(), itemList, this);

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
        itemList.add(new Item("Phụ kiện",R.drawable.vo));

    }

    @Override
    public void onItemClick(int position) {

        switch (position) {
            case 0: {
                Intent intent = new Intent(getContext(), Categories.class);
                intent.putExtra("type", "shirts");
                startActivityForResult(intent,300);
                break;
            }
            case 1: {
                Intent intent = new Intent(getContext(), Categories.class);
                intent.putExtra("type", "pants");
                startActivityForResult(intent,300);
                break;
            }
            case 2: {
                Intent intent = new Intent(getContext(), Categories.class);
                intent.putExtra("type", "accessories");
                startActivityForResult(intent,300);
                break;
            }
        }
        /*itemList.get(position);
        Intent intent = new Intent(getContext(), Categories.class);
        intent.putExtra("type", "shrits");
        startActivity(intent);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300) {
            ((MainActivity)getActivity()).getBadge();
        }
    }
}

