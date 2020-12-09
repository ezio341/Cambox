package com.example.cambox.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cambox.R;
import com.example.cambox.adapter.OrderTabAdapter;
import com.example.cambox.databinding.FragmentOrderBinding;
import com.example.cambox.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;
    private static User user;
    private String[] tabTitle = new String[]{"Delivered", "Processing"};

    public OrderFragment(User user) {
        this.user = user;
    }

    public OrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        // Inflate the layout for this fragment
        OrderTabAdapter adapter = new OrderTabAdapter(this, user, binding.tabLayout.getTabCount());
        binding.viewPager2.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int pos) {
                tab.setText(tabTitle[pos]);
            }
        }).attach();
        return binding.getRoot();
    }
}