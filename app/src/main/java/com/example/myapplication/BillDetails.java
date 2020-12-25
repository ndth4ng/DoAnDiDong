package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BillDetails extends AppCompatActivity {

    ImageView back;

    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    BillAdapter billAdapter;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        //getList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public void AnhXa() {
        recyclerView = findViewById(R.id.listView);
        back = findViewById(R.id.back);
    }

    /*public void getList() {
        if (userId != null) {
            Query query = fStore.collection("users").document(userId).collection("Cart");

            FirestoreRecyclerOptions<CartItem> options = new FirestoreRecyclerOptions.Builder<CartItem>()
                    .setLifecycleOwner(this)
                    .setQuery(query, new SnapshotParser<CartItem>() {
                        @NonNull
                        @Override
                        public CartItem parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            //Product product = snapshot.toObject(Product.class);
                            CartItem cartItem = snapshot.toObject(CartItem.class);
                            String itemId = snapshot.getId();
                            cartItem.setItemId(itemId);
                            return cartItem;
                        }
                    })
                    .build();

            billAdapter = new BillAdapter(options);

            recyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(billAdapter);
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}