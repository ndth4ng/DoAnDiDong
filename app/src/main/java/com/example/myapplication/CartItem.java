package com.example.myapplication;

import java.io.Serializable;

public class CartItem extends Product implements Serializable {

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    private String size;
    private long amount;

    public CartItem() {}

    public CartItem(String name, String image, long price, String itemId) {
        super(name, image, price, itemId);
    }

    public CartItem(String name, String image, long price, String itemId, String size, long amount) {
        super(name, image, price, itemId);
        this.size = size;
        this.amount = amount;
    }
}
