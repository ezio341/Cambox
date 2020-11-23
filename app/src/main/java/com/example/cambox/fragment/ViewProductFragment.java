package com.example.cambox.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cambox.R;
import com.example.cambox.databinding.FragmentViewProductBinding;
import com.example.cambox.model.Product;


public class ViewProductFragment extends Fragment {
    FragmentViewProductBinding binding;
    Product product;

    public ViewProductFragment(Product product) {
        this.product = product;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}