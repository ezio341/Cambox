package com.example.cambox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cambox.databinding.ItemCartBinding;
import com.example.cambox.interfaces.OnClickListenerCart;
import com.example.cambox.model.Cart;
import com.example.cambox.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<Cart> cartlist;
    OnClickListenerCart listener;
    DatabaseReference ref;

    public CartAdapter(List<Cart> cartlist) {
        this.cartlist = cartlist;
        ref = FirebaseDatabase.getInstance().getReference();
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

        public void bind(final Cart cart){
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Product p = snapshot.child("Item").child(cart.getProduct()).getValue(Product.class);
                    Glide.with(binding.getRoot().getContext()).load(p.getImg_token()).into(binding.imageView3);
                    binding.setProduct(p);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            binding.setCart(cart);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
