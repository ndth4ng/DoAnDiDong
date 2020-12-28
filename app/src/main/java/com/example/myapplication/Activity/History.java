package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Adapter.HistoryAdapter;
import com.example.myapplication.Model.HistoryItem;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class History extends AppCompatActivity implements HistoryAdapter.OnListItemClick {

    ImageButton back;
    TextView tvHistory;

    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    HistoryAdapter historyAdapter;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        getList();
    }

    void AnhXa() {
        recyclerView = findViewById(R.id.recycleViewHistory);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    void getList() {
        if (userId != null ) {
            Query query = fStore.collection("users").document(userId).collection("HistoryBills");

            FirestoreRecyclerOptions<HistoryItem> options = new FirestoreRecyclerOptions.Builder<HistoryItem>()
                    .setLifecycleOwner(this)
                    .setQuery(query, new SnapshotParser<HistoryItem>() {
                        @NonNull
                        @Override
                        public HistoryItem parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            String itemId = snapshot.getId();
                            Timestamp timestamp = (Timestamp) snapshot.getData().get("time");

                            String name = snapshot.get("nameCustomer").toString();
                            String address = snapshot.get("addressCustomer").toString();
                            String phone = snapshot.get("phoneCustomer").toString();

                            HistoryItem historyItem = new HistoryItem(itemId,name,address,phone,timestamp.toDate());

                            return historyItem;
                        }
                    })
                    .build();

            historyAdapter = new HistoryAdapter(options, this);

            recyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setAdapter(historyAdapter);
        }
    }


    @Override
    public void onItemClick(HistoryItem snapshot, int position) {
        Intent i = new Intent(getApplicationContext(), BillDetails.class);
        i.putExtra("customer",snapshot);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}