package com.example.cambox.model;

import com.example.cambox.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Cart extends BaseObservable {
    private String product;
    private int amount;
    private String key;

    public Cart() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Bindable
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        notifyPropertyChanged(BR.amount);
    }

    public Cart(String product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Cart(String product, int amount, String key) {
        this.product = product;
        this.amount = amount;
        this.key = key;
    }
}
