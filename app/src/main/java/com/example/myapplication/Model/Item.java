package com.example.myapplication.Model;

public class Item {

    String name;
    int image;

    public Item(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public  Item() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


}
