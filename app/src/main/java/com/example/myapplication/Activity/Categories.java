package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.example.myapplication.Adapter.FirestoreAdapter;
import com.example.myapplication.Adapter.ItemAdapter;
import com.example.myapplication.Adapter.SearchAdapter;
import com.example.myapplication.Model.Item;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Categories extends AppCompatActivity implements FirestoreAdapter.OnListItemClick, SearchAdapter.OnItemListener {

    EditText search;

    ImageView back;
    FirebaseFirestore fStore;
    FirestoreAdapter adapter;
    RecyclerView recyclerView;

    SearchAdapter searchAdapter;

    List<Product> productList = new ArrayList<>();

    private static final int REQUEST_CODE_BADGE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        AnhXa();

        // Nhan data loai san pham

        fStore = FirebaseFirestore.getInstance();

        Client client = new Client("ORJFV2EGF6", "6f76d69840d48e299529c8021b06a20c");
        final Index index = client.getIndex("products");

        final String type = getType();
        final String cate = getCate();

        /*fStore.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> doc = new HashMap<>();
                        doc.put("itemId",document.getId());
                        doc.put("name",document.get("name").toString());
                        doc.put("price",document.get("price").toString());
                        doc.put("image",document.get("image").toString());
                        doc.put("type",document.get("type").toString());
                        doc.put("cate",document.get("cate").toString());
                        index.addObjectAsync(new JSONObject(doc), null);
                    }
                }
            }
        });*/

        //Query
        final Query query;

        if (type != null) {
            query = fStore.collection("products").whereEqualTo("type", type);
        } else {
            query = fStore.collection("products").whereEqualTo("cate", cate);
        }

        //Toast.makeText(this, "Type: " + type + " Cate: " + cate, Toast.LENGTH_SHORT).show();


        // Danh sach san pham
        getList(query);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    CompletionHandler completionHandler = new CompletionHandler() {
                        @Override
                        public void requestCompleted(JSONObject content, AlgoliaException error) {
                            try {

                                JSONArray hits = content.getJSONArray("hits");
                                productList = new ArrayList<>();

                                for (int i = 0; i < hits.length(); i++) {
                                    JSONObject jsonObject = hits.getJSONObject(i);
                                    //Log.d("TAG","Product type1: "+ jsonObject.getString("type"));
                                    if (jsonObject.getString("type").equals(type) || jsonObject.getString("cate").equals(cate)) {
                                        Log.d("TAG","Object name: " + jsonObject.getString("name"));
                                        Product product = new Product(jsonObject.getString("itemId"), jsonObject.getString("image"), jsonObject.getString("name"), jsonObject.getInt("price"));
                                        productList.add(product);
                                    }
                                }

                                searchAdapter = new SearchAdapter(getApplicationContext(), productList, Categories.this);

                                GridLayoutManager gridLayoutManager = new GridLayoutManager(Categories.this, 2, GridLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setAdapter(searchAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    String text = editable.toString();

                    //Đưa từ khoá search vào query
                    index.searchAsync(new com.algolia.search.saas.Query(text), completionHandler);

                } else {
                    getList(query);
                }
            }
        });
    }

    protected void getList(Query query) {
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    protected void AnhXa() {
        recyclerView = findViewById(R.id.productList);
        back = findViewById(R.id.back);
        search = findViewById(R.id.edtSearch);
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
        if (requestCode == REQUEST_CODE_BADGE && resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK);
        }
    }

    //RecyclerView Click
    @Override
    public void onItemClick(Product snapshot, int position) {
        Log.d("ITEM_CLICK", "Chọn sản phẩm : " + position + " ID: " + snapshot.getItemId());
        getProductDetail(snapshot.getItemId());
    }

    //Search RecyclerView Click
    @Override
    public void onItemClick(int position) {
        getProductDetail(productList.get(position).getItemId());
    }

    private void getProductDetail(String ItemID) {
        fStore.collection("products").document(ItemID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                Product product = snapshot.toObject(Product.class);
                product.setItemId(snapshot.getId());

                Intent i = new Intent(getApplicationContext(), ProductDetail.class);

                i.putExtra("Product", product);
                startActivityForResult(i, REQUEST_CODE_BADGE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Khong the lay du lieu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}