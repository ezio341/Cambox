package com.example.cambox.adapter;

import com.example.cambox.fragment.OrderDeliveredFragment;
import com.example.cambox.fragment.OrderProcessingFragment;
import com.example.cambox.model.User;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrderTabAdapter extends FragmentStateAdapter {
    private int length;
    private User user;

    public OrderTabAdapter(@NonNull Fragment fragment, User user, int length) {
        super(fragment);
        this.user = user;
        this.length = length;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                OrderDeliveredFragment deliver = new OrderDeliveredFragment(user);
                return deliver;
            case 1:
                OrderProcessingFragment process = new OrderProcessingFragment(user);
                return process;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return length;
    }
}
