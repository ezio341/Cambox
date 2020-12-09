package com.example.cambox.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cambox.R;
import com.example.cambox.adapter.CheckoutAdapter;
import com.example.cambox.databinding.FragmentOrderDetailsBinding;
import com.example.cambox.model.Cart;
import com.example.cambox.model.Courier;
import com.example.cambox.model.Order;
import com.example.cambox.model.Product;
import com.example.cambox.model.Profile;
import com.example.cambox.model.User;
import com.example.cambox.util.FormatUtil;
import com.example.cambox.util.FragmentUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderDetailsFragment extends Fragment {
    private FragmentOrderDetailsBinding binding;
    private User user;
    private DatabaseReference ref;
    private Order order;
    private ProgressDialog pg;

    public OrderDetailsFragment(User user, Order order) {
        this.user = user;
        this.order = order;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference();
        pg = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false);
        binding.setUser(user);
        binding.setOrder(order);
        binding.setCurrencyformat(FormatUtil.getCurrencyFormat());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pg.setMessage("Loading ...|");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.show();
        if(order.getStatus().equalsIgnoreCase("Processed")){
            binding.orderstatusProcessed.setVisibility(View.VISIBLE);
            binding.txtProcessDate.setText(order.getOrderDate());
        }else{
            binding.orderalertstatusComplete.setVisibility(View.VISIBLE);
            binding.txtReceivedDate.setText(order.getReceiveDate());
        }
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.child("Profile").child(user.getKey()).getValue(Profile.class);
                int total = 0;
                //count total price on order cart list
                for(Cart c:order.getCart()){
                    int price = Integer.valueOf(snapshot.child("Item").child(c.getProduct()).child("price").getValue(String.class));
                    total += price*c.getAmount();
                }
                binding.setTotal(total);
                binding.setProfile(profile);
                binding.setCourier(order.getCourier());
                CheckoutAdapter adapter = new CheckoutAdapter(order.getCart());
                LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                binding.rvOrderDetails.setAdapter(adapter);
                binding.rvOrderDetails.setLayoutManager(layout);
                pg.dismiss();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.btnBackOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.getFragment(new OrderFragment(user), getActivity());
            }
        });
    }
}