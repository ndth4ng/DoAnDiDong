package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    // List sản phẩm
    private ArrayList<Product> listProduct;
    // Lưu context đê dễ truy cập
    private Context context;

    public ProductAdapter(Context context, ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.cell_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        // Gan du lieu
        Product product = listProduct.get(position);
        holder.nameProduct.setText(product.getName());
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.priceProduct.setText(currencyFormatter.format(product.getPrice()));
        holder.imgProduct.setImageResource(product.getImage());
    }

    @Override
    public int getItemCount() {
        return listProduct.size(); // tra ve item tai vi tri posision
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView nameProduct, priceProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Anh xa

            imgProduct = itemView.findViewById(R.id.imageProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
        }
    }
}
