package com.example.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.Activity.ProductDetail;
import com.example.myapplication.Adapter.SearchAdapter;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Search extends Fragment implements SearchAdapter.OnItemListener {
    View view;
    List<Product> productList;
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    EditText search;

    FirebaseFirestore fStore;

    private static final int REQUEST_CODE_BADGE = 300;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);
        ((MainActivity) getActivity()).getBadge();

        AnhXa();


        fStore = FirebaseFirestore.getInstance();

        Client client = new Client("ORJFV2EGF6", "6f76d69840d48e299529c8021b06a20c");
        final Index index = client.getIndex("products");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {

                    CompletionHandler completionHandler = new CompletionHandler() {
                        @Override
                        public void requestCompleted(@Nullable JSONObject content, @Nullable AlgoliaException error) {
                            try {
                                JSONArray hits = content.getJSONArray("hits");
                                productList = new ArrayList<>();

                                for (int i = 0; i < hits.length(); i++) {
                                    JSONObject jsonObject = hits.getJSONObject(i);
                                    //Log.d("TAG","Product type1: "+ jsonObject.getString("type"));
                                    Log.d("TAG", "Object name: " + jsonObject.getString("name"));
                                    Product product = new Product(jsonObject.getString("itemId"), jsonObject.getString("image"), jsonObject.getString("name"), jsonObject.getInt("price"));
                                    productList.add(product);
                                }

                                searchAdapter = new SearchAdapter(getContext(), productList, Search.this);

                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setAdapter(searchAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    String text = charSequence.toString();
                    //Đưa từ khoá search vào query
                    index.searchAsync(new com.algolia.search.saas.Query(text), completionHandler);
                } else {
                    recyclerView.setAdapter(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return view;
    }

    private void AnhXa() {
        recyclerView = view.findViewById(R.id.recycleViewSearch);
        search = view.findViewById(R.id.edtSearch);
    }


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
    public void onResume() {
        super.onResume();
        search.requestFocus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BADGE && resultCode == Activity.RESULT_OK) {
            resetBadge();
        }
    }
}