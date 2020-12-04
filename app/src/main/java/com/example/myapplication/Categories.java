package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Categories extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Product> productArrayList;
    ProductAdapter productAdapter;
    ImageView back;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        AnhXa();

        fStore = FirebaseFirestore.getInstance();




        productArrayList = new ArrayList<>();

        productArrayList.add(new Product("Áo", 300000, R.drawable.ao));
        productArrayList.add(new Product("Áo", 400000, R.drawable.ao));
        productArrayList.add(new Product("Áo", 500000, R.drawable.ao));
        productArrayList.add(new Product("Áo", 300000, R.drawable.ao));
        productArrayList.add(new Product("Áo", 400000, R.drawable.ao));
        productArrayList.add(new Product("Áo", 500000, R.drawable.ao));
        productArrayList.add(new Product("Áo", 300000, R.drawable.ao));
        productArrayList.add(new Product("Áo", 400000, R.drawable.ao));
        productArrayList.add(new Product("Áo", 500000, R.drawable.ao));

        productAdapter = new ProductAdapter(this,productArrayList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(productAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void getView() {


    }

    protected void AnhXa() {
        recyclerView = findViewById(R.id.productList);
        back = findViewById(R.id.back);
    }
}