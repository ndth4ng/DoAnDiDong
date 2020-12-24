package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Categories extends AppCompatActivity implements FirestoreAdapter.OnListItemClick {

    //ArrayList<Product> productArrayList;
    //ProductAdapter productAdapter;
    ImageView back;
    FirebaseFirestore fStore;
    FirestoreAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        AnhXa();

        // Nhan data loai san pham

        fStore = FirebaseFirestore.getInstance();

        String type = getType();
        String cate = getCate();

        //Query
        Query query;

        if (type != null) {
             query = fStore.collection("products").whereEqualTo("type",type);
        }
        else {
            query = fStore.collection("products").whereEqualTo("cate",cate);
        }

        Toast.makeText(this, "Type: " + type + " Cate: " + cate, Toast.LENGTH_SHORT).show();

        /*PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(6) // Số item muốn load lần đầu tiên
                .setPageSize(4) //Số item muốn load khi kéo trang xuống
                .build();*/

        //RecyclerOptions
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

        adapter = new FirestoreAdapter(options, this);


        recyclerView.setHasFixedSize(true);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void AnhXa() {
        recyclerView = findViewById(R.id.productList);
        back = findViewById(R.id.back);
    }

    protected String getCate() {
        Intent data = getIntent();
        String cate = data.getStringExtra("cate");
        return cate;
    }

    protected String getType() {
        Intent data = getIntent();
        String type = data.getStringExtra("type");
        return type;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            setResult(300);
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

                Intent i = new Intent(getApplicationContext(), ProductDetail.class);

                /*i.putExtra("id", product.getItemId());
                i.putExtra("image", product.getImage());
                i.putExtra("name", product.getName());
                i.putExtra("detail", product.getDetail());
                i.putExtra("price", String.valueOf(product.getPrice()));*/

                i.putExtra("Product", product);
                startActivityForResult(i, 200);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Khong the lay du lieu",Toast.LENGTH_SHORT).show();
            }
        });
    }
}