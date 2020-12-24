package com.example.myapplication;

import java.io.Serializable;

public class Product implements Serializable {

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    private String itemId;
    private String cate;
    private String detail;
    private String image;
    private String name;
    private long price;

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Product() {}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product(String cate, String detail, String image, String name, long price, String itemId) {
        this.cate = cate;
        this.detail = detail;
        this.image = image;
        this.name = name;
        this.price = price;
        this.itemId = itemId;
    }

    public Product(String cate, String detail, String image, String name, long price) {
        this.cate = cate;
        this.detail = detail;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public Product(String name, String image, long price, String itemId) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.itemId = itemId;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }
}
