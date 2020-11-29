package com.example.cambox.model;

public class Courier {
    private String name;
    private int price;

    public Courier(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Courier() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
