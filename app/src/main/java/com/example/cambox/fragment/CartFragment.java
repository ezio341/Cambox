package com.example.cambox.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Looper;
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
import com.example.cambox.util.FragmentUtil;
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
    private FragmentCartBinding binding;
    private static User user;
    private List<Cart> cartList;
    private DatabaseReference ref;
    private CartAdapter adapter;
    private ProgressDialog pg;

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
        pg = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pg.setMessage("Loading ...");
        pg.show();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        // Inflate the layout for this fragment
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.child("Cart").child(user.getKey()).getChildren()){
                    Cart cart = new Cart(
                            data.child("product").getValue(String.class),
                            data.child("amount").getValue(Integer.class),
                            data.getKey());
                    cartList.add(cart);
                }
                if(cartList.size() == 0){
                    binding.emptyCartMsg.setVisibility(View.VISIBLE);
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
                            ref.child("Cart").child(user.getKey()).child(cart.getKey()).setValue(cart);
                        }
                    }

                    @Override
                    public void delete(final Cart cart) {
                        final ProgressDialog pg = new ProgressDialog(getContext());
                        pg.setTitle("Deleting");
                        pg.setMessage("Please Wait ...");
                        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        pg.show();
                        DatabaseReference delRef = ref.child("Cart").child(user.getKey()).child(cart.getKey());
                        delRef.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                FragmentUtil.getFragment(new CartFragment(user), getActivity());
                                Toast.makeText(getContext(), "Product "
                                        +snapshot.child("Item").child(cart.getProduct()).child("name").getValue(String.class)
                                        +" is removed", Toast.LENGTH_SHORT).show();
                                pg.dismiss();
                            }
                        });
                    }
                });
                pg.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnCheckoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartList.size() == 0){
                    Toast.makeText(getContext(), "Your Cart is Empty!", Toast.LENGTH_SHORT).show();
                }else {
                    pg.setMessage("Loading ...");
                    pg.show();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String address = snapshot.child("Profile").child(user.getKey()).child("address").getValue(String.class);
                            if(!address.equals("address not set") && !address.isEmpty()){
                                FragmentUtil.getFragment(new CheckoutFragment(user, cartList), getActivity());
                                Log.d("size list", cartList.size()+"");
                            }else{
                                Toast.makeText(getContext(), "Please Edit Your Profile First!", Toast.LENGTH_SHORT).show();
                            }
                            pg.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

}