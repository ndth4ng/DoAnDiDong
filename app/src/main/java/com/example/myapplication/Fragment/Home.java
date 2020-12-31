package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myapplication.Adapter.ItemAdapter;
import com.example.myapplication.Activity.Categories;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.Model.Item;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment implements ItemAdapter.OnItemListener {
    View view;
    List<Item> itemList;
    RecyclerView recyclerView;
    ItemAdapter mAdapter;

    private static final int REQUEST_CODE_BADGE = 300;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, container, false);

        recyclerView = view.findViewById(R.id.listItem);

        getList();

        return view;
    }

    public void getList() {
        itemList = new ArrayList<>();

        itemList.add(new Item("Áo",R.drawable.ao));
        itemList.add(new Item("Quần",R.drawable.quan));
        itemList.add(new Item("Phụ kiện",R.drawable.vo));

        mAdapter = new ItemAdapter(getContext(), itemList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(mAdapter);

        ((MainActivity)getActivity()).getBadge();
    }

    @Override
    public void onItemClick(int position) {

        switch (position) {
            case 0: {
                Intent intent = new Intent(getContext(), Categories.class);
                intent.putExtra("type", "shirts");
                startActivityForResult(intent,REQUEST_CODE_BADGE);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            }
            case 1: {
                Intent intent = new Intent(getContext(), Categories.class);
                intent.putExtra("type", "pants");
                startActivityForResult(intent,REQUEST_CODE_BADGE);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            }
            case 2: {
                Intent intent = new Intent(getContext(), Categories.class);
                intent.putExtra("type", "accessories");
                startActivityForResult(intent,REQUEST_CODE_BADGE);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BADGE && resultCode == Activity.RESULT_OK) {
           ((MainActivity)getActivity()).getBadge();
        }
    }
}

