package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Adapter.BillAdapter;
import com.example.myapplication.Model.CartItem;
import com.example.myapplication.Model.HistoryItem;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class BillDetails extends AppCompatActivity {

    TextView nameCus, addressCus, phoneCus, timeBill, totalPrice, idBill;
    ImageView back;

    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    BillAdapter billAdapter;
    String userId;
    long total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        Log.d("TAG","Use ID BIll: " +userId);

        Intent data = getIntent();

        HistoryItem item = (HistoryItem) data.getSerializableExtra("customer");

        getInfor(item);
        getList(item);
        getTotal(item);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void getInfor(HistoryItem item) {
        nameCus.setText(item.getNameCustomer());
        addressCus.setText(item.getAddressCustomer());
        phoneCus.setText(item.getPhoneCustomer());

        Locale locale = new Locale("vn","VN");
        DateFormat dateFormat = DateFormat.getDateTimeInstance(2,3,locale);
        timeBill.setText(dateFormat.format(item.getTime()));
        idBill.setText("Mã đơn hàng: " + item.getItemId());
    }

    public void AnhXa() {
        back = findViewById(R.id.back);

        nameCus = findViewById(R.id.name);
        addressCus = findViewById(R.id.address);
        phoneCus = findViewById(R.id.phone_number);

        idBill = findViewById(R.id.idBill);
        timeBill = findViewById(R.id.timeBill);
        totalPrice = findViewById(R.id.totalPrice);

        recyclerView = findViewById(R.id.recycleViewHistoryBill);
    }

    public void getList(HistoryItem item) {
        if (userId != null) {
            Query query = fStore.collection("users").document(userId).collection("HistoryBills").document(item.getItemId()).collection(item.getItemId());
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

    private void getTotal(HistoryItem item) {
        total = 0;
        fStore.collection("users").document(userId).collection("HistoryBills").document(item.getItemId()).collection(item.getItemId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}