package com.example.cambox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cambox.databinding.ItemCartBinding;
import com.example.cambox.interfaces.OnClickListenerCart;
import com.example.cambox.model.Cart;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<Cart> cartlist;
    OnClickListenerCart listener;

    public CartAdapter(List<Cart> cartlist) {
        this.cartlist = cartlist;
    }

    public void setListener(OnClickListenerCart listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCartBinding binding = ItemCartBinding.inflate(inflater, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Cart cart = cartlist.get(position);
        holder.bind(cart);
    }

    @Override
    public int getItemCount() {
        return cartlist != null ? cartlist.size() : 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;

        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Cart cart){
            Glide.with(binding.getRoot().getContext()).load(cart.getProduct().getImg_token()).into(binding.imageView3);
            binding.setCart(cart);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
