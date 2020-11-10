package com.example.cambox.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cambox.MainActivity;
import com.example.cambox.R;
import com.example.cambox.databinding.FragmentProfileBinding;
import com.example.cambox.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    User user;

    public ProfileFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setUser(user);
        return binding.getRoot();
    }

}