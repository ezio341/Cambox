package com.example.cambox.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cambox.R;
import com.example.cambox.databinding.ItemCheckoutBinding;
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

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutAdapter extends RecyclerView.Adapter <CheckoutAdapter.CheckoutViewHolder> {
    private List<Cart> cartlist;
    private DatabaseReference ref;
    private StorageReference stg;

    public CheckoutAdapter(List<Cart> cartlist) {
        this.cartlist = cartlist;
        ref = FirebaseDatabase.getInstance().getReference();
        stg = FirebaseStorage.getInstance().getReference();
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
                    HashMap<String, String> p= (HashMap) snapshot.child("Item").child(cart.getProduct()).getValue();
                    final Product products = new Product(p.get("name"), p.get("desc"), p.get("img"), Integer.valueOf(p.get("price")), p.get("product_date"),
                            Integer.valueOf(p.get("stock")), Double.valueOf(p.get("disc")), p.get("key"));
                    stg.child("item_image/"+products.getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(binding.getRoot().getContext()).load(uri).into(binding.imgCktProduct);
                            binding.progressBar5.setVisibility(View.GONE);
                        }
                    });
                    binding.setProduct(products);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            binding.setCurrencyformat(FormatUtil.getCurrencyFormat());
            binding.setCart(cart);
            binding.executePendingBindings();
        }
    }
}
