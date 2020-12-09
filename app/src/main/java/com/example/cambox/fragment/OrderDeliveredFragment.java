package com.example.cambox.fragment;

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
import com.example.cambox.adapter.OrderRvAdapter;
import com.example.cambox.databinding.FragmentOrderDeliveredBinding;
import com.example.cambox.interfaces.OnClickListenerOrder;
import com.example.cambox.model.Order;
import com.example.cambox.model.User;
import com.example.cambox.util.FragmentUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDeliveredFragment extends Fragment {
    private User user;
    private FragmentOrderDeliveredBinding binding;
    private List<Order> deliveredList;
    private DatabaseReference ref;

    public OrderDeliveredFragment(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deliveredList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_delivered, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.child("Order").child(user.getKey()).getChildren()){
                    Order order = data.getValue(Order.class);
                    if(order.getStatus().equalsIgnoreCase("Delivered")){
                        deliveredList.add(order);
                    }
                }
                if(deliveredList.size()==0){
                    binding.txtEmptyDelivered.setVisibility(View.VISIBLE);
                }
                binding.rvDelivered.setLayoutManager(new LinearLayoutManager(getContext()));
                OrderRvAdapter adapter = new OrderRvAdapter(deliveredList);
                binding.rvDelivered.setAdapter(adapter);
                adapter.setListener(new OnClickListenerOrder() {
                    @Override
                    public void onClickDetail(Order order) {
                        FragmentUtil.getFragment(new OrderDetailsFragment(user, order), getActivity());
                    }
                });
                binding.progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}