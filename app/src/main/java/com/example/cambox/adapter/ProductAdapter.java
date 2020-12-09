package com.example.cambox.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cambox.R;
import com.example.cambox.databinding.ItemProductBinding;
import com.example.cambox.interfaces.OnClickListenerProduct;
import com.example.cambox.model.Product;
import com.example.cambox.model.User;
import com.example.cambox.util.FormatUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> list;
    private OnClickListenerProduct listener;
    private StorageReference stg;
    private DatabaseReference ref;
    private User user;

    public ProductAdapter(List<Product> list, User user) {
        this.list = list;
        stg = FirebaseStorage.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference();
        this.user = user;
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
        public void bind(final Product product){
//            Log.d("download url", stg.child("item_image").getDownloadUrl().toString());
            stg.child("item_image/"+product.getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(binding.getRoot().getContext()).load(uri).into(binding.imageView2);
                    binding.progressBar3.setVisibility(View.GONE);
                }
            });
            ref.child("Favorite").child(user.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(product.getKey()).exists()){
                        binding.imageButton.setImageResource(R.drawable.ic_baseline_favorite_pink_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            binding.setPriceformat(FormatUtil.getCurrencyFormat());
            binding.setProduct(product);
            binding.setListener(listener);
            binding.executePendingBindings();
        }

    }
}
