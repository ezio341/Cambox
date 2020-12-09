package com.example.cambox.model;

import java.util.List;

public class Order {
    private List<Cart> cart;
    private Courier courier;
    private String status;
    private String id;
    private String orderDate;
    private String receiveDate;
    private int orderNum;
    private int total;

    public Order() {
    }

    public Order(List<Cart> cart, Courier courier, String status, String id, String orderDate, int orderNum, int total) {
        this.cart = cart;
        this.courier = courier;
        this.status = status;
        this.id = id;
        this.orderDate = orderDate;
        this.orderNum = orderNum;
        this.total = total;
    }

    public Order(List<Cart> cart, Courier courier, String status, String id, String orderDate, String receiveDate, int orderNum, int total) {
        this.cart = cart;
        this.courier = courier;
        this.status = status;
        this.id = id;
        this.orderDate = orderDate;
        this.receiveDate = receiveDate;
        this.orderNum = orderNum;
        this.total = total;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
