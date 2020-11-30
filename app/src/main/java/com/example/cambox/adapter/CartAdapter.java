package com.example.cambox.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cambox.R;
import com.example.cambox.databinding.ItemCartBinding;
import com.example.cambox.interfaces.OnClickListenerCart;
import com.example.cambox.model.Cart;
import com.example.cambox.model.Product;
import com.example.cambox.util.FormatUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    List<Cart> cartlist;
    OnClickListenerCart listener;
    DatabaseReference ref;
    StorageReference stg;

    public CartAdapter(List<Cart> cartlist) {
        this.cartlist = cartlist;
        ref = FirebaseDatabase.getInstance().getReference();
        stg = FirebaseStorage.getInstance().getReference();
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
            binding.imageView3.setImageResource(R.drawable.ic_baseline_cached_24);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Product p = snapshot.child("Item").child(cart.getProduct()).getValue(Product.class);
                    stg.child("item_image/"+p.getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(binding.getRoot().getContext()).load(uri).into(binding.imageView3);
                        }
                    });
                    binding.setProduct(p);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            binding.setCurrencyformat(FormatUtil.getCurrencyFormat());
            binding.setCart(cart);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
