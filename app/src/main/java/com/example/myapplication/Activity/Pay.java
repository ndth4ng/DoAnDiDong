package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.BillAdapter;
import com.example.myapplication.Model.CartItem;
import com.example.myapplication.R;
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
import com.google.firebase.firestore.SetOptions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
        getTotal();

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
                setResult(255);
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
            Log.d("TAG", "ID Bill: " +doc.getId());
        final Map<String, Object> inforBill = new HashMap<>();
        inforBill.put("time", Calendar.getInstance().getTime());
        inforBill.put("nameCustomer", nameUser.getText().toString());
        inforBill.put("addressCustomer", addressUser.getText().toString());
        inforBill.put("phoneCustomer", phoneUser.getText().toString());

        final CollectionReference addRef = fStore.collection("bills");

        final CollectionReference cartRef = fStore.collection("users")
                .document(userId)
                .collection("Cart");

        final CollectionReference historyRef = fStore.collection("users")
                .document(userId)
                .collection("HistoryBills");

        cartRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    historyRef.document(doc.getId()).set(inforBill, SetOptions.merge());
                    addRef.document(doc.getId()).set(inforBill, SetOptions.merge());

                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> item = new HashMap<>();

                        item.put("name", document.get("name"));
                        item.put("size",document.get("size"));
                        item.put("amount",document.get("amount"));
                        item.put("price",document.get("price"));
                        item.put("image",document.get("image"));

                        historyRef.document(doc.getId())
                                .collection(doc.getId())
                                .document(document.getId())
                                .set(item)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "[User History] User ID: "+ userId + " them sp: " + document.getId() + "vao Database");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Loi them sp vao csdl: " +e);
                                    }
                                });

                        addRef.document(doc.getId()).collection(doc.getId()).document(document.getId()).set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "[Database] User ID: "+ userId + " them sp: " + document.getId() + "vao Database");
                                cartRef.document(document.getId()).delete();
                            }
                        }). addOnFailureListener(new OnFailureListener() {
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

    private void getTotal() {
        total = 0;
        fStore.collection("users").document(userId).collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot snapshot : task.getResult()) {
                        //Toast.makeText(Pay.this,snapshot.getId(),Toast.LENGTH_SHORT).show();
                        Log.d("TAG","Snapshot ID: " +snapshot.getId());
                        total += (Long.valueOf(snapshot.get("amount").toString()) * Long.valueOf(snapshot.get("price").toString()));

                        Locale locale = new Locale("vn","VN");
                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                        totalPrice.setText("Tổng tiền: "+ currencyFormatter.format(total));
                    }
                }
            }
        });
    }

    public void getList() {
        if (userId != null) {
            Query query = fStore.collection("users").document(userId).collection("Cart").orderBy("name");

            FirestoreRecyclerOptions<CartItem> options = new FirestoreRecyclerOptions.Builder<CartItem>()
                    .setLifecycleOwner(this)
                    .setQuery(query, new SnapshotParser<CartItem>() {
                        @NonNull
                        @Override
                        public CartItem parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            CartItem cartItem = snapshot.toObject(CartItem.class);
                            String itemId = snapshot.getId();
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}