package com.example.cambox.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cambox.R;
import com.example.cambox.databinding.FragmentAccInfoBinding;
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

import java.util.HashMap;

public class AccInfoFragment extends Fragment {
    private FragmentAccInfoBinding binding;
    private static User user;
    DatabaseReference ref;

    public AccInfoFragment(User user) {
        this.user = user;
    }

    public AccInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_acc_info, container, false);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.child("Profile").child(user.getKey()).getValue(Profile.class);
                HashMap<String, String> wmap = (HashMap) snapshot.child("Wallet").child(user.getKey()).getValue();
                Wallet wallet = new Wallet(Long.valueOf(wmap.get("balance")));
                binding.setUser(user);
                binding.setProfile(profile);
                binding.setWallet(wallet);
                binding.setCurrencyformat(FormatUtil.getCurrencyFormat());
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
        binding.btnBackAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.getFragment(new ProfileFragment(user), getActivity());
            }
        });
    }
}