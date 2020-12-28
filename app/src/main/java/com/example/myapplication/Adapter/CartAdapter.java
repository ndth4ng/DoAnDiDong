package com.example.myapplication.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myapplication.Model.CartItem;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class CartAdapter extends FirestoreRecyclerAdapter<CartItem, CartAdapter.CartItemViewHolder> {

    private OnListItemClick onListItemClick;

    public CartAdapter(@NonNull FirestoreRecyclerOptions<CartItem> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart,parent,false);
        return new CartItemViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CartItemViewHolder holder, int position, @NonNull final CartItem model) {
        holder.nameProduct.setText(model.getName());

        Picasso.get().load(model.getImage()).resize(450,500).centerCrop().into(holder.imgProduct);

        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.priceProduct.setText(currencyFormatter.format(model.getPrice() * model.getAmount()));

        holder.sizeProduct.setText(model.getSize());

        holder.quantity.setNumber(String.valueOf(model.getAmount()));
    }

   /* @Override
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
    }*/

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ElegantNumberButton.OnValueChangeListener {

        ImageView imgProduct, del;
        TextView nameProduct, priceProduct, sizeProduct;
        ElegantNumberButton quantity;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Anh xa
            imgProduct = itemView.findViewById(R.id.imageProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
            sizeProduct = itemView.findViewById(R.id.sizeProduct);
            quantity = itemView.findViewById(R.id.quantity);

            del = itemView.findViewById(R.id.del);

            imgProduct.setOnClickListener(this);

            quantity.setOnValueChangeListener(this);

            del.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view.equals(del)) {
                onListItemClick.onDelClick(getItem(getAdapterPosition()), getAdapterPosition());
            } else if (view.equals(imgProduct)){
            onListItemClick.onItemClick(getItem(getAdapterPosition()), getAdapterPosition());
            }
        }

        @Override
        public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
            onListItemClick.onValueChangeClick(getItem(getAdapterPosition()),getAdapterPosition(),newValue);
            Log.d("TAG", "Gia tri: " + newValue);
        }
    }

    public interface OnListItemClick {
        void onItemClick(CartItem snapshot, int position);
        void onDelClick(CartItem snapshot, int position);
        void onValueChangeClick(CartItem snapshot, int position, long newValue);
    }
}
