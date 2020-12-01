package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class Categories extends AppCompatActivity {
    GridView gridView;
    ArrayList<Product> productArrayList = new ArrayList<>();
    ProductAdapter productAdapter;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        gridView = findViewById(R.id.gridviewCategories);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        productArrayList.add(new Product("Áo", "300.000", R.drawable.ao));
        productArrayList.add(new Product("Áo", "300.000", R.drawable.ao));
        productArrayList.add(new Product("Áo", "300.000", R.drawable.ao));
        productArrayList.add(new Product("Áo", "300.000", R.drawable.ao));
        productArrayList.add(new Product("Áo", "300.000", R.drawable.ao));
        productArrayList.add(new Product("Áo", "300.000", R.drawable.ao));

        productAdapter = new ProductAdapter(Categories.this, R.layout.cell_product, productArrayList);
        gridView.setAdapter(productAdapter);

    }
}