package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    LayoutInflater inflater;
    private Context context;
    int layout;
    private List<Product> productList;
    ArrayList<Product> arrayList;

    public ProductAdapter(Context context, int layout, List<Product> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }
    public ProductAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.arrayList = list;
        inflater = LayoutInflater.from(context);
    }
    private class ViewHolder{
        ImageView img;
        TextView tv_name, tv_price;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ProductAdapter.ViewHolder holder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            //ánh xạ view
            holder.tv_name = convertView.findViewById(R.id.nameCategories);
            holder.tv_price = convertView.findViewById(R.id.priceCategories);
            holder.img = convertView.findViewById(R.id.imageViewCategories);
            convertView.setTag(holder);
        }
        else {
            holder = (ProductAdapter.ViewHolder) convertView.getTag();
        }
        Product furniture = productList.get(position);
        holder.tv_name.setText(furniture.getName());
        holder.tv_price.setText(furniture.getPrice());
        holder.img.setImageResource(furniture.getImage());
        return convertView;
    }
}
