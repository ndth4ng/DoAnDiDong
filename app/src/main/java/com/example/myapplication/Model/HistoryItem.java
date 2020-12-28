package com.example.myapplication.Model;

import com.google.type.DateTime;

import java.io.Serializable;
import java.util.Date;

public class HistoryItem implements Serializable {

    String nameCustomer = "";
    String addressCustomer = "";
    String phoneCustomer = "";
    Date time = getTime();
    String itemId = "";


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public HistoryItem() {

    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getAddressCustomer() {
        return addressCustomer;
    }

    public void setAddressCustomer(String addressCustomer) {
        this.addressCustomer = addressCustomer;
    }

    public String getPhoneCustomer() {
        return phoneCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.phoneCustomer = phoneCustomer;
    }


    public HistoryItem(String itemId, String nameCustomer, String addressCustomer, String phoneCustomer, Date time) {
        this.time = time;
        this.itemId = itemId;
        this.nameCustomer = nameCustomer;
        this.addressCustomer = addressCustomer;
        this.phoneCustomer = phoneCustomer;
    }
}
