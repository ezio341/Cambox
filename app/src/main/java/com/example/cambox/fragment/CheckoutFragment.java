package com.example.cambox.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cambox.R;
import com.example.cambox.adapter.CheckoutAdapter;
import com.example.cambox.databinding.FragmentCheckoutBinding;
import com.example.cambox.databinding.ItemCheckoutBinding;
import com.example.cambox.model.Cart;
import com.example.cambox.model.Courier;
import com.example.cambox.model.Profile;
import com.example.cambox.model.User;
import com.example.cambox.model.Wallet;
import com.example.cambox.util.FormatUtil;
import com.example.cambox.util.FragmentUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class CheckoutFragment extends Fragment {
    FragmentCheckoutBinding binding;
    private User user;
    private List<Cart> cartList;
    private DatabaseReference ref;

    public CheckoutFragment(User user, List<Cart> cartList) {
        this.user = user;
        this.cartList = cartList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false);
        binding.setCurrencyformat(FormatUtil.getCurrencyFormat());
        CheckoutAdapter adapter = new CheckoutAdapter(cartList);
        binding.rvCheckout.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCheckout.setAdapter(adapter);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Wallet wallet = snapshot.child("Wallet").child(user.getKey()).getValue(Wallet.class);
                Courier courier = snapshot.child("Courier").child("0").getValue(Courier.class);
                Profile profile = snapshot.child("Profile").child(user.getKey()).getValue(Profile.class);
                binding.setProfile(profile);
                binding.setWallet(wallet);
                binding.setCourier(courier);
                int totalprice =0;
                for(Cart c : cartList){
                    int price = snapshot.child("Item").child(c.getProduct()).child("price").getValue(Integer.class) * c.getAmount();
                    totalprice += price;
                }
                binding.setTotalprice(totalprice);

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
        binding.btnBackCkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.getFragment(new CartFragment(user), getActivity());
            }
        });
    }
}