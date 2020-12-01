package com.example.myapplication;

public class Product {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    String name;
    String price;
    int image;

    public Product(String name, String price, int image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }
}
