package com.example.cambox.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cambox.R;
import com.example.cambox.adapter.ProductAdapter;
import com.example.cambox.databinding.FragmentFavoriteBinding;
import com.example.cambox.model.Product;
import com.example.cambox.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {
    User user;
    FragmentFavoriteBinding binding;
    List<Product> productList;
    public FavoriteFragment(User user){
        this.user = user;
        this.productList = new ArrayList<>();
    }
    public FavoriteFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        // Inflate the layout for this fragment
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        binding.rvFavorite.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(final DataSnapshot i:snapshot.child("Favorite").child(user.getKey()).getChildren()){
                    Product p = snapshot.child("Item").child(i.getKey()).getValue(Product.class);
                    productList.add(p);
                    binding.rvFavorite.setAdapter(new ProductAdapter(productList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}