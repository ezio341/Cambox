package com.example.cambox.interfaces;

import android.widget.ImageButton;

import com.example.cambox.model.Cart;

public interface OnClickListenerCart {
    void setAmount(Cart cart, int amount, ImageButton btnIncrease, ImageButton btnDecrease);
    void delete(Cart cart);
}
