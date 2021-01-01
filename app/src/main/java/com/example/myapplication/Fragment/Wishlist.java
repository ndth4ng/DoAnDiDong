package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.WishlistAdapter;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.Model.Product;
import com.example.myapplication.Activity.ProductDetail;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Wishlist extends Fragment implements WishlistAdapter.OnListItemClick {
    View view;
    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView unText;

    String userId;

    WishlistAdapter adapter;

    private static final int REQUEST_CODE_BADGE = 300;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wishlist, container, false);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();

        if (user == null) {

        } else {
            if (user.isAnonymous()) {
                unText.setVisibility(View.VISIBLE);
            }  else {
                unText.setVisibility(View.INVISIBLE);
                getList();
            }
        }
        //Toast.makeText(getActivity(),"User ID: "+ fAuth.getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();

        return view;
    }

    public void AnhXa() {
        recyclerView = view.findViewById(R.id.wishlist);
        unText = view.findViewById(R.id.unText);
    }

    public void getList() {

            userId = fAuth.getCurrentUser().getUid();
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

            recyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(adapter);

            ((MainActivity)getActivity()).getBadge();
        }

    @Override
    public void onItemClick(Product snapshot, int position) {
        Log.d("ITEM_CLICK","Chọn sản phẩm : " + position + " ID: " + snapshot.getItemId());
        fStore.collection("products").document(snapshot.getItemId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                Product product = snapshot.toObject(Product.class);
                product.setItemId(snapshot.getId());

                final Intent i = new Intent(getActivity(), ProductDetail.class);

                i.putExtra("Product", product);

                startActivityForResult(i, REQUEST_CODE_BADGE);

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
        if (requestCode == REQUEST_CODE_BADGE && resultCode == Activity.RESULT_OK) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity)getActivity()).getBadge();
                }
            }, 200); // 5000ms delay
        }
    }
}