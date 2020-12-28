package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.CartItem;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetail extends AppCompatActivity {
    ImageView imageProduct, back, inc, dec, favorite;
    TextView nameProduct, detailProduct, priceProduct, quantity;
    Button add;
    RadioButton r_s, r_m, r_l;
    RadioGroup size_group;
    String size_product = "M";

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

        //Thay doi size san pham
        size_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case (R.id.size_S) :
                        size_product = "S";
                        Toast.makeText(ProductDetail.this,"S",Toast.LENGTH_SHORT).show();
                        break;
                    case (R.id.size_M) :
                        size_product = "M";
                        Toast.makeText(ProductDetail.this,"M",Toast.LENGTH_SHORT).show();
                        break;
                    case (R.id.size_L) :
                        size_product = "L";
                        Toast.makeText(ProductDetail.this,"L",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Them sp vao gio hang
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCart(product, size_product);
                onBackPressed();
            }
        });
    }

    public void AddToCart(Product product, final String size_product) {
        final Product pro = product;
        final String userID = fAuth.getCurrentUser().getUid();
        DocumentReference productIdRef = fStore.collection("users").document(userID).collection("Cart").document(pro.getItemId().concat(size_product));
        productIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {

                        final Long newValue = Long.valueOf(snapshot.get("amount").toString());
                        final Long newValue2 = Long.valueOf(quantity.getText().toString());
                        final Long newValue3 = newValue + newValue2;

                        fStore.collection("users").document(userID).collection("Cart").document(snapshot.getId()).update("amount",newValue3);

                        Toast.makeText(ProductDetail.this,"Old amount: " + newValue + " Add amount: " + newValue2 + " Total: " + newValue3, Toast.LENGTH_SHORT).show();
                    } else {
                        CartItem cartItem = new CartItem(pro.getName(),pro.getImage(),pro.getPrice(),pro.getItemId());
                        cartItem.setAmount(Long.valueOf(quantity.getText().toString()));
                        cartItem.setSize(size_product);

                        fStore.collection("users").document(userID).collection("Cart").document(cartItem.getItemId().concat(size_product)).set(cartItem);

                        Toast.makeText(ProductDetail.this,"Thêm sp vào Cart",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

        favorite = findViewById(R.id.favorite);
        add = findViewById(R.id.addtocart);
        size_group = findViewById(R.id.group_size);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_out_down);
    }
}