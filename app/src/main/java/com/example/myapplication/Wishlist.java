package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Wishlist extends Fragment implements WishlistAdapter.OnListItemClick {
    View view;
    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    WishlistAdapter wishlistAdapter;
    String userId;
    LinearLayout linearLayout;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wishlist, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        Toast.makeText(getActivity(), "UserID: " + userId, Toast.LENGTH_SHORT).show();

        AnhXa();

        getList();

        return view;
    }

    public void AnhXa() {
        recyclerView = view.findViewById(R.id.wishlist);
        linearLayout = view.findViewById(R.id.layout);
    }



    public void getList() {
        if (userId != null) {
            Query query = fStore.collection("users").document(userId).collection("Favorites");

            PagedList.Config config = new PagedList.Config.Builder()
                    .setInitialLoadSizeHint(6) // Số item muốn load lần đầu tiên
                    .setPageSize(4) //Số item muốn load khi kéo trang xuống
                    .build();

            FirestorePagingOptions<Product> options = new FirestorePagingOptions.Builder<Product>()
                    .setLifecycleOwner(this)
                    .setQuery(query, config, new SnapshotParser<Product>() {
                        @NonNull
                        @Override
                        public Product parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            Product product = snapshot.toObject(Product.class);
                            String itemId = snapshot.getId();
                            product.setItemId(itemId);
                            return product;
                        }
                    })
                    .build();

            wishlistAdapter = new WishlistAdapter(options, this);

            recyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(wishlistAdapter);
        }
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Log.d("ITEM_CLICK","Chọn sản phẩm : " + position + " ID: " + snapshot.getId());
        fStore.collection("products").document(snapshot.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                Product product = snapshot.toObject(Product.class);
                product.setItemId(snapshot.getId());

                Intent i = new Intent(getActivity(), ProductDetail.class);

                i.putExtra("Product", product);
                startActivityForResult(i, 100);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Khong the lay du lieu",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            getList();
        }
    }
}