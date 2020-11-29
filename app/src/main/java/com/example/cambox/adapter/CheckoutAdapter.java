package com.example.cambox.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cambox.databinding.ItemCheckoutBinding;
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

public class CheckoutAdapter extends RecyclerView.Adapter <CheckoutAdapter.CheckoutViewHolder> {
    List<Cart> cartlist;
    DatabaseReference ref;

    public CheckoutAdapter(List<Cart> cartlist) {
        this.cartlist = cartlist;
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public CheckoutAdapter.CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCheckoutBinding binding = ItemCheckoutBinding.inflate(inflater, parent, false);
        return new CheckoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutAdapter.CheckoutViewHolder holder, int position) {
        Cart cart = cartlist.get(position);
        holder.bind(cart);
    }

    @Override
    public int getItemCount() {
        return cartlist != null ? cartlist.size():0;
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ItemCheckoutBinding binding;

        public CheckoutViewHolder(ItemCheckoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(final Cart cart){
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Product p = snapshot.child("Item").child(cart.getProduct()).getValue(Product.class);
                    Glide.with(binding.getRoot().getContext()).load(p.getImg_token()).into(binding.imgCktProduct);
                    binding.setProduct(p);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            binding.setCart(cart);
            binding.executePendingBindings();
        }
    }
}
