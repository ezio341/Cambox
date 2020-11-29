package com.example.cambox.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cambox.R;
import com.example.cambox.databinding.ItemProductBinding;
import com.example.cambox.interfaces.OnClickListenerProduct;
import com.example.cambox.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> list;
    private OnClickListenerProduct listener;

    public ProductAdapter(List<Product> list) {
        this.list = list;
    }

    public void setListener(OnClickListenerProduct listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProductBinding binding = ItemProductBinding.inflate(inflater, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = list.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }else{
            return 0;
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Product product){
            Glide.with(binding.getRoot().getContext()).load(product.getImg_token()).into(binding.imageView2);
            binding.setProduct(product);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
