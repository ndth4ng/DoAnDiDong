package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Cart extends Fragment implements CartAdapter.OnListItemClick {
    View view;
    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    CartAdapter cartAdapter;
    String userId;
    Button btnPay;

    private static final int REQUEST_CODE_BADGE = 300;
    //private static final int REQUEST_CODE_PAY = 300;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cart, container, false);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        Toast.makeText(getActivity(), "UserID: " + userId, Toast.LENGTH_SHORT).show();

        getList();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Pay.class);
                startActivityForResult(i, REQUEST_CODE_BADGE);
            }
        });

        return view;
    }

    public void getList() {
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

            cartAdapter = new CartAdapter(options, this);

            recyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(cartAdapter);

            ((MainActivity) getActivity()).getBadge();
        }
    }


    public void AnhXa() {
        recyclerView = view.findViewById(R.id.listView);
        btnPay = view.findViewById(R.id.pay);
    }

    @Override
    public void onItemClick(CartItem snapshot, int position) {
        Log.d("ITEM_CLICK", "Chọn sản phẩm : " + position + " ID: " + snapshot.getItemId());
        String productID = snapshot.getItemId().substring(0, snapshot.getItemId().length() - 1);
        fStore.collection("products").document(productID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                Product product = snapshot.toObject(Product.class);
                product.setItemId(snapshot.getId());

                Intent i = new Intent(getActivity(), ProductDetail.class);

                i.putExtra("Product", product);
                startActivityForResult(i, REQUEST_CODE_BADGE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Khong the lay du lieu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void resetBadge() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) getActivity()).getBadge();
            }
        }, 200); // 5000ms delay
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_BADGE && resultCode == Activity.RESULT_OK)
            resetBadge();
    }

    @Override
    public void onDelClick(CartItem snapshot, int position) {
        fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("Cart").document(snapshot.getItemId()).delete();
        getList();
        Toast.makeText(getActivity(), "Xoa khoi cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValueChangeClick(CartItem snapshot, int position, long newValue) {
        fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("Cart").document(snapshot.getItemId()).update("amount", newValue);
        Toast.makeText(getActivity(), "Product ID:" + snapshot.getItemId(), Toast.LENGTH_SHORT).show();
    }
}