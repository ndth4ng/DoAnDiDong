package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.internal.cache.DiskLruCache;

public class ProductDetail extends AppCompatActivity {
    ImageView imageProduct, back, inc, dec, cart, favorite;
    TextView nameProduct, detailProduct, priceProduct, quantity;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        final String userID = fAuth.getCurrentUser().getUid();

        Intent data = getIntent();
        final Product product = (Product) data.getSerializableExtra("Product");

        //Toast.makeText(this, "Product ID: " + product.getItemId(), Toast.LENGTH_SHORT).show();

        Picasso.get().load(product.getImage()).into(imageProduct);

        nameProduct.setText(product.getName());
        detailProduct.setText(product.getDetail());

        Locale locale = new Locale("vn", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        priceProduct.setText(currencyFormatter.format(product.getPrice()));

        // ==================================//

        DocumentReference docRef = fStore.collection("users").document(userID).collection("Favorites").document(product.getItemId());
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    // Lấy sản phẩm
                    final DocumentReference docRef = fStore.collection("products").document(product.getItemId());

                    if (value.exists()) {
                        // Nếu document tồn tài thị sẽ có sự kiện Xóa nếu click
                        favorite.setImageResource(R.drawable.ic_baseline_favorite_24);

                        favorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //DocumentReference docRef = fStore.collection("products").document(product.getItemId());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        fStore.collection("users").document(userID).collection("Favorites").document(product.getItemId()).delete();
                                        favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                        Toast.makeText(ProductDetail.this,"Xóa sp khỏi ds yêu thích",Toast.LENGTH_SHORT).show();

                                        // Xoa san pham set Ket qua tra ve de reset view
                                        setResult(100);

                                        Log.d("TAG", "Delete Product ID: " + snapshot.getId() + " from Favorites");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Loi");
                                    }
                                });
                            }
                        });
                    } else {
                        // Ngược lại là sự kiện Thêm
                        favorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //DocumentReference docRef = fStore.collection("products").document(product.getItemId());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        fStore.collection("users").document(userID).collection("Favorites").document(product.getItemId()).set(product);
                                        favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                                        Toast.makeText(ProductDetail.this,"Thêm sp vào ds yêu thích",Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "Add Product ID: " + snapshot.getId() + " to Favorites");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Loi");
                                    }
                                });
                            }
                        });
                    }


                }
            }
        });

        // Nut Back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Cart
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Cart()).addToBackStack(null).commit();
            }
        });

        // Tang so luong san pham
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long quant = Long.valueOf(quantity.getText().toString()) + 1;
                quantity.setText(quant.toString());
            }
        });

        // Giam so luong san pham
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long quant = Long.valueOf(quantity.getText().toString());
                if (quant != 1) {
                    quant -= 1;
                    quantity.setText(quant.toString());
                }
            }
        });

        //Yeu thich


    }

    protected void AnhXa() {
        imageProduct = findViewById(R.id.imageProduct);
        nameProduct = findViewById(R.id.nameProduct);
        detailProduct = findViewById(R.id.detailProduct);
        priceProduct = findViewById(R.id.priceProduct);

        back = findViewById(R.id.back);
        inc = findViewById(R.id.inc);
        dec = findViewById(R.id.dec);
        quantity = findViewById(R.id.quantity);
        cart = findViewById(R.id.cart);
        favorite = findViewById(R.id.favorite);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }



}