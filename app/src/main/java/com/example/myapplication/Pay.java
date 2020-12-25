package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

public class Pay extends AppCompatActivity {

    ImageView back;
    TextView totalPrice;
    EditText nameUser, addressUser, phoneUser;
    Button pay;

    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    BillAdapter billAdapter;
    String userId;

    long total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        //Toast.makeText(Pay.this, "User ID: " + userId, Toast.LENGTH_SHORT).show();

        getInfor();

        getList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateBill();
                setResult(Activity.RESULT_OK);
                //finish();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000); // 5000ms delay
            }
        });
    }

    public void CreateBill() {

        final DocumentReference doc = fStore.collection("bills").document();
            Log.d("TAG", "ID Bill: "+doc.getId());
        final CollectionReference addRef = fStore.collection("bills").document(doc.getId()).collection(doc.getId());
        final CollectionReference cartRef = fStore.collection("users").document(userId).collection("Cart");
        cartRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        addRef.add(document).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("TAG", "Them sp: " + document.getId() + "vao Database");
                                cartRef.document(document.getId()).delete();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "Loi them sp vao csdl: " +e);
                            }
                        });
                    }
                } else {
                    Log.d("TAG", "Loi trong qua trinh tao don hang");
                }
            }
        });
    }

    public void AnhXa() {
        recyclerView = findViewById(R.id.listView);

        nameUser = findViewById(R.id.name);
        addressUser = findViewById(R.id.address);
        phoneUser = findViewById(R.id.phone_number);

        totalPrice = findViewById(R.id.totalPrice);
        back = findViewById(R.id.back);
        pay = findViewById(R.id.pay);
    }

    public void getInfor() {
        DocumentReference inforRef = fStore.collection("users").document(userId);
        inforRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userInfor = task.getResult();
                    nameUser.setText(userInfor.get("Tên").toString());
                    addressUser.setText(userInfor.get("Địa chỉ").toString());
                    phoneUser.setText(userInfor.get("Số điện thoại").toString());
                }
            }
        });
    }

    public void getList() {
        if (userId != null) {
            Query query = fStore.collection("users").document(userId).collection("Cart").orderBy("size");
            total = 0;

            FirestoreRecyclerOptions<CartItem> options = new FirestoreRecyclerOptions.Builder<CartItem>()
                    .setLifecycleOwner(this)
                    .setQuery(query, new SnapshotParser<CartItem>() {
                        @NonNull
                        @Override
                        public CartItem parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            CartItem cartItem = snapshot.toObject(CartItem.class);
                            String itemId = snapshot.getId();
                            total += (Long.valueOf(snapshot.get("amount").toString()) * Long.valueOf(snapshot.get("price").toString()));

                            Locale locale = new Locale("vn","VN");
                            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                            totalPrice.setText("Tổng tiền: "+ currencyFormatter.format(total));

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
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}