package com.example.cambox.model;

public class Product {
    String name;
    String desc;
    String img;
    String img_token;
    String price;
    String product_date;
    String stock;
    String discount;

    public String getImg_token() {
        return img_token;
    }

    public void setImg_token(String img_token) {
        this.img_token = img_token;
    }

    public Product() {
    }

    public Product(String name, String desc, String img, String img_token, String price, String product_date, String stock, String discount) {
        this.name = name;
        this.desc = desc;
        this.img = img;
        this.img_token = img_token;
        this.price = price;
        this.product_date = product_date;
        this.stock = stock;
        this.discount = discount;
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

    public String getPrice() {
        if(price!=null){
            price = "Rp "+price;
        }
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
