package com.example.cambox.interfaces;

import android.widget.ImageButton;

import com.example.cambox.model.Product;

public interface OnClickListenerProduct {
    void onclick(Product product);
    void onClickFavorite(Product product, ImageButton btn);
}
