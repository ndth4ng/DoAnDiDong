package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class WishlistAdapter extends FirestoreRecyclerAdapter<Product, WishlistAdapter.ProductViewHolder> {

    private OnListItemClick onListItemClick;

    public WishlistAdapter(@NonNull FirestoreRecyclerOptions<Product> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
        holder.nameProduct.setText(model.getName());
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.priceProduct.setText(currencyFormatter.format(model.getPrice()));
        holder.detailProduct.setText(model.getDetail());

        if (!model.getImage().equals("")) {
            Picasso.get().load(model.getImage()).resize(450, 500).centerCrop().into(holder.imgProduct);
        } else {
            Picasso.get().load(R.drawable.error).resize(450, 500).centerCrop().into(holder.imgProduct);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wishlist,parent,false);
        return new ProductViewHolder(view);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgProduct;
        TextView nameProduct, priceProduct, detailProduct;
        LinearLayout linearLayout;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            // Anh xa
            imgProduct = itemView.findViewById(R.id.imageProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
            detailProduct = itemView.findViewById(R.id.detailProduct);

            linearLayout = itemView.findViewById(R.id.linearLayout);

            imgProduct.setOnClickListener(this);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(Product snapshot, int position);
    }
}
