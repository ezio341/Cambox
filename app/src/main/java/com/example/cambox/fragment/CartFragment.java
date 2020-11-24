package com.example.cambox.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cambox.R;
import com.example.cambox.adapter.CartAdapter;
import com.example.cambox.databinding.FragmentCartBinding;
import com.example.cambox.interfaces.OnClickListenerCart;
import com.example.cambox.model.Cart;
import com.example.cambox.model.Product;
import com.example.cambox.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    FragmentCartBinding binding;
    User user;
    List<Cart> cartList;
    DatabaseReference ref;
    CartAdapter adapter;

    public CartFragment(User user) {
        this.user = user;
    }

    public CartFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference();
        cartList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        // Inflate the layout for this fragment
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_key = user.getKey();
                for(DataSnapshot data : snapshot.child("Cart").child(user_key).getChildren()){
                    Cart cart = data.getValue(Cart.class);
                    cartList.add(cart);
                }

                adapter = new CartAdapter(cartList);
                binding.rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rvCart.setAdapter(adapter);
                adapter.setListener(new OnClickListenerCart() {
                    @Override
                    public void setAmount(Cart cart, int amount, ImageButton btnIncrease, ImageButton btnDecrease) {
                        if(amount < 1){
                            btnDecrease.setEnabled(false);
                        }else{
                            btnDecrease.setEnabled(true);
                            cart.setAmount(amount);
//                            ref.child("Cart").child(user.getKey()).child(cart.getKey()).setValue(cart);
                        }
                    }

                    @Override
                    public void delete(Cart cart) {
                        DatabaseReference delRef = ref.child("Cart").child(user.getKey()).child(cart.getKey());
                        delRef.removeValue();
                        Toast.makeText(getContext(), "Product "+cart.getProduct().getName()+" is removed", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}