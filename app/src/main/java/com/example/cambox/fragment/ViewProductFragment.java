package com.example.cambox.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cambox.R;
import com.example.cambox.databinding.FragmentViewProductBinding;
import com.example.cambox.model.Cart;
import com.example.cambox.model.Product;
import com.example.cambox.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewProductFragment extends Fragment {
    FragmentViewProductBinding binding;
    Product product;
    Fragment backDirection;
    DatabaseReference ref;
    User user;

    public ViewProductFragment(Product product, Fragment fragmentDirection) {
        this.product = product;
        this.backDirection = fragmentDirection;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference();
        user = (User) getArguments().getParcelable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_product, container, false);
        binding.setProduct(product);
        Glide.with(binding.getRoot().getContext()).load(product.getImg_token()).into(binding.imgProduct);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragment(backDirection);
            }
        });
        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Cart cart = new Cart(product, 1);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isCartAvailable = false;
                        List<Cart> cartList = new ArrayList<>();
                        for(DataSnapshot data : snapshot.child("Cart").child(user.getKey()).getChildren()){
                            Cart dbCart = data.getValue(Cart.class);
                            cartList.add(dbCart);
                        }
                        for(Cart c : cartList){
                            if(c.getProduct().getKey().equals(product.getKey())){
                                isCartAvailable = true;
                                Toast.makeText(getContext(), "Product is already available in Cart", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if(!isCartAvailable){
                            cartList.add(new Cart(product, 1, ""+cartList.size()));
                            ref.child("Cart").child(user.getKey()).setValue(cartList);
                            Toast.makeText(getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });
    }

    private void getFragment(Fragment fragment){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();
    }
}