package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Furniture implements Serializable {
    String name;
    String des;
    Number price;
    int image;
    String strImage;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Furniture(String name, String des,Number price, int image) {
        this.name = name;
        this.des = des;
        this.image = image;
        this.price = price;
    }
    public Furniture(String name, String des,Number price, String image) {
        this.name = name;
        this.des = des;
        this.strImage = image;
        this.price = price;
    }

}
