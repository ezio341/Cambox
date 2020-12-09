package com.example.cambox.model;

public class Product {
    String name;
    String desc;
    String img;
    int price;
    String product_date;
    int stock;
    double disc;
    String key;

    public Product() {
    }

    public Product(String name, String desc, String img, int price, String product_date, int stock, double disc, String key) {
        this.name = name;
        this.desc = desc;
        this.img = img;
        this.price = price;
        this.product_date = product_date;
        this.stock = stock;
        this.disc = disc;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getDisc() {
        return disc;
    }

    public void setDisc(double disc) {
        this.disc = disc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
