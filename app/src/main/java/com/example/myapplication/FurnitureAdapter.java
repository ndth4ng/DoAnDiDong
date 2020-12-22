/*
package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FurnitureAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private Context context;
    int layout;
    private List<Furniture> furnitureList;
    ArrayList<Furniture> arrayList;

    public FurnitureAdapter(Context context, int layout, List<Furniture> furnitureList) {
        this.context = context;
        this.layout = layout;
        this.furnitureList = furnitureList;
    }
    public FurnitureAdapter(Context context, ArrayList<Furniture> list) {
        this.context = context;
        this.arrayList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return furnitureList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView img;
        TextView tv_name, tv_description, tv_price;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
           */
/* LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            //ánh xạ view
            holder.tv_name = convertView.findViewById(R.id.name_pro);
            holder.tv_description = convertView.findViewById(R.id.des_pro);
            holder.tv_price = convertView.findViewById(R.id.price_pro);
            holder.img = convertView.findViewById(R.id.image_pro);
            convertView.setTag(holder);*//*

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


      */
/*  Furniture furniture = furnitureList.get(position);
        holder.tv_name.setText(furniture.getName());
        holder.tv_description.setText(furniture.getDes());
        holder.tv_price.setText(furniture.getPrice().toString());
        holder.img.setImageResource(furniture.getImage());
        return convertView;*//*

    }



}
*/
