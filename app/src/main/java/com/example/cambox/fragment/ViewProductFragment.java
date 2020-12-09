package com.example.cambox.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cambox.R;
import com.example.cambox.databinding.FragmentViewProductBinding;
import com.example.cambox.model.Cart;
import com.example.cambox.model.Product;
import com.example.cambox.model.User;
import com.example.cambox.util.FormatUtil;
import com.example.cambox.util.FragmentUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ViewProductFragment extends Fragment {
    private FragmentViewProductBinding binding;
    private Product product;
    private Fragment backDirection;
    private DatabaseReference ref;
    private static User user;
    private ProgressDialog pg;
    private StorageReference stg;

    public ViewProductFragment(Product product, Fragment fragmentDirection) {
        this.product = product;
        this.backDirection = fragmentDirection;
    }

    public ViewProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference();
        user = (User) getArguments().getParcelable("user");
        pg = new ProgressDialog(getContext());
        stg = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_product, container, false);
        binding.setProduct(product);
        binding.setCurrencyformat(FormatUtil.getCurrencyFormat());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stg.child("item_image/"+product.getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(binding.getRoot().getContext()).load(uri).into(binding.imgProduct);
                binding.progressBar6.setVisibility(View.GONE);
            }
        });
        ref.child("Favorite").child(user.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(product.getKey()).exists()){
                    binding.btnFavoriteProduct.setImageResource(R.drawable.ic_baseline_favorite_pink_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.getFragment(backDirection, getActivity());
            }
        });
        binding.btnFavoriteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Favorite").child(user.getKey()).child(product.getKey()).exists()){
                            ref.child("Favorite").child(user.getKey()).child(product.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    binding.btnFavoriteProduct.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                }
                            });
                        }else{
                            ref.child("Favorite").child(user.getKey()).child(product.getKey()).setValue(product.getKey()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    binding.btnFavoriteProduct.setImageResource(R.drawable.ic_baseline_favorite_pink_24);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            pg.setTitle("Adding to Your Cart");
            pg.setMessage("Please Wait ...");
            pg.show();
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
                        if(c.getProduct().equals(product.getKey())){
                            isCartAvailable = true;
                            Toast.makeText(getContext(), "Product is already available in Cart", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(!isCartAvailable){
                        cartList.add(new Cart(product.getKey(), 1, ""+cartList.size()));
                        ref.child("Cart").child(user.getKey()).setValue(cartList);
                        Toast.makeText(getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                    }
                    pg.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

            }
        });
    }
}