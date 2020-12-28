package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.HistoryItem;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.util.Locale;

public class HistoryAdapter extends FirestoreRecyclerAdapter<HistoryItem, HistoryAdapter.HistoryViewHolder> {
    private OnListItemClick onListItemClick;

    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<HistoryItem> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull HistoryViewHolder holder, int position, @NonNull HistoryItem model) {
        Locale locale = new Locale("vn","VN");
        DateFormat dateFormat = DateFormat.getDateTimeInstance(1,3,locale);
        holder.historyBill.setText("Đơn hàng ngày " + dateFormat.format(model.getTime()));
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history,parent,false);
        return new HistoryViewHolder(view);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView historyBill;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            // Anh xa
            historyBill = itemView.findViewById(R.id.historyBill);
            historyBill.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(HistoryItem snapshot, int position);
    }
}
