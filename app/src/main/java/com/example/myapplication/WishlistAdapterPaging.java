package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class WishlistAdapterPaging extends FirestorePagingAdapter<Product, WishlistAdapterPaging.ProductViewHolder> {

    private OnListItemClick onListItemClick;

    public WishlistAdapterPaging(@NonNull FirestorePagingOptions<Product> options, OnListItemClick onListItemClick) {
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

        Picasso.get().load(model.getImage()).resize(450,500).centerCrop().into(holder.imgProduct);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wishlist,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state) {
            case LOADING_INITIAL:
                Log.d("PAGING_LOG","Bắt đầu hiển thị sản phẩm");
                break;
            case LOADING_MORE:
                Log.d("PAGING_LOG","Hiển thị sản phẩm tiếp theo.");
                break;
            case FINISHED:
                Log.d("PAGING_LOG","Đã hiển thị tất cả sản phẩm.");
                break;
            case ERROR:
                Log.d("PAGING_LOG","Lỗi hiển thị dữ liệu.");
                break;
            case LOADED:
                Log.d("PAGING_LOG","Tổng số sản phẩm : " + getItemCount());
                break;

        }
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
        void onItemClick(DocumentSnapshot snapshot, int position);
    }
}
