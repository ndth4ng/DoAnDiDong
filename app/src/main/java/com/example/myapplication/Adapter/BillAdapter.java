package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.CartItem;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.NumberFormat;
import java.util.Locale;

public class BillAdapter extends FirestoreRecyclerAdapter<CartItem, BillAdapter.CartItemViewHolder> {

    public BillAdapter(@NonNull FirestoreRecyclerOptions<CartItem> options) {
        super(options);
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bill,parent,false);
        return new CartItemViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CartItemViewHolder holder, int position, @NonNull final CartItem model) {
        holder.nameProduct.setText(model.getName());

        //Picasso.get().load(model.getImage()).resize(450,500).centerCrop().into(holder.imgProduct);

        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.priceProduct.setText(currencyFormatter.format(model.getPrice() * model.getAmount()));
        holder.quantityProduct.setText(String.valueOf(model.getAmount()));
        holder.sizeProduct.setText(model.getSize());

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder {

        //ImageView imgProduct;
        TextView nameProduct, priceProduct, sizeProduct, quantityProduct;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Anh xa
            //imgProduct = itemView.findViewById(R.id.imageProduct);
            quantityProduct = itemView.findViewById(R.id.quantityProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
            sizeProduct = itemView.findViewById(R.id.sizeProduct);
        }
    }
}
