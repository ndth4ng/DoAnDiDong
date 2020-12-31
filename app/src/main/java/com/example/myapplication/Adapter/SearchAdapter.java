package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.CompletionHandler;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    // List sản phẩm
    private Context context;
    private List<Product> listItem;
    // Lưu context đê dễ truy cập
    private OnItemListener onItemListener;

    public SearchAdapter(Context context, List<Product> listItem, OnItemListener onItemListener) {
        this.context = context;
        this.listItem = listItem;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.cell_product, parent, false);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        // Gan du lieu
        Product product = listItem.get(position);
        holder.nameProduct.setText(product.getName());
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.priceProduct.setText(currencyFormatter.format(product.getPrice()));

        Picasso.get().load(product.getImage()).resize(450,500).centerCrop().into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgProduct;
        TextView nameProduct, priceProduct;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            // Anh xa
            imgProduct = itemView.findViewById(R.id.imageProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);

            linearLayout = itemView.findViewById(R.id.linearLayout);

            linearLayout.setOnClickListener(this);
            imgProduct.setOnClickListener(this);
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
