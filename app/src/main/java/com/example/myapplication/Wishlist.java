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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Wishlist extends Fragment implements WishlistAdapter.OnListItemClick {
    View view;
    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userId;

    WishlistAdapter adapter;

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
    }



    public void getList() {
        if (userId != null) {
            Query query = fStore.collection("users").document(userId).collection("Favorites");

            FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                    .setLifecycleOwner(this)
                    .setQuery(query, new SnapshotParser<Product>() {
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

            adapter = new WishlistAdapter(options, this);

            /*PagedList.Config config = new PagedList.Config.Builder()
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
                    .build();*/

            //wishlistAdapter = new WishlistAdapter(options, this);

            recyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            getList();
        }
    }

    @Override
    public void onItemClick(Product snapshot, int position) {
        Log.d("ITEM_CLICK","Chọn sản phẩm : " + position + " ID: " + snapshot.getItemId());
        fStore.collection("products").document(snapshot.getItemId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
}