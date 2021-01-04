package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Item;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    // List sản phẩm
    private Context context;
    private List<Item> listItem;
    // Lưu context đê dễ truy cập
    private OnItemListener onItemListener;

    public ItemAdapter(Context context, List<Item> listItem, OnItemListener onItemListener) {
        this.context = context;
        this.listItem = listItem;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.cell_categories, parent, false);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        // Gan du lieu
        Item item = listItem.get(position);
        holder.nameItem.setText(item.getName());
        holder.imgItem.setImageResource(item.getImage());
    }

    @Override
    public int getItemCount() {
        return listItem.size(); // tra ve item tai vi tri posision
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgItem;
        TextView nameItem;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            // Anh xa

            imgItem = itemView.findViewById(R.id.slide);
            nameItem = itemView.findViewById(R.id.tvItem);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}
