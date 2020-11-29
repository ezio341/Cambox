package com.example.cambox.model;

public class Wallet {
    private long balance;

    public Wallet() {
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Wallet(long balance) {
        this.balance = balance;
    }
}
