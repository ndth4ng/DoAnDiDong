package com.example.myapplication.Activity;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.HistoryAdapter;
import com.example.myapplication.Model.HistoryItem;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class History extends AppCompatActivity implements HistoryAdapter.OnListItemClick {

    ImageButton back;
    Button delHistory;

    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    HistoryAdapter historyAdapter;
    String userId;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        AnhXa();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        getList();

        delHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doubleBackToExitPressedOnce) {
                    deleteHistoryBill();
                    Log.d("TAG","Delete History Bill");
                    return;
                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(History.this, "Bấm lần nữa để xóa lich sử đặt hàng", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        });
    }

    void AnhXa() {
        recyclerView = findViewById(R.id.recycleViewHistory);
        delHistory = findViewById(R.id.delHistory);

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
            Query query = fStore.collection("users").document(userId).collection("HistoryBills").orderBy("time", Query.Direction.DESCENDING);

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

    private void deleteHistoryBill() {
        fStore.collection("users").document(userId).collection("HistoryBills").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                        Log.d("TAG","Snapshot ID ne: "+snapshot.getId());
                        fStore.collection("users").document(userId).collection("HistoryBills").document(snapshot.getId()).delete();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(HistoryItem snapshot, int position) {
        Intent i = new Intent(getApplicationContext(), BillDetails.class);
        i.putExtra("customer",snapshot);
        startActivityForResult(i,321);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 321 && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}