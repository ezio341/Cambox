package com.example.cambox.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cambox.Login;
import com.example.cambox.R;
import com.example.cambox.databinding.FragmentProfileBinding;
import com.example.cambox.model.Profile;
import com.example.cambox.model.User;
import com.example.cambox.util.FragmentUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private DatabaseReference ref;
    private FragmentProfileBinding binding;
    private User user;

    public ProfileFragment(User user) {
        this.user = user;
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setUser(user);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ref.child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.child(user.getKey()).getValue(Profile.class);
                binding.setProfile(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.getFragment(new EditProfileFragment(user), getActivity());
            }
        });
        binding.btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Account")
                        .setMessage("Are You Sure to delete this account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ref.child("Account").child(user.getKey()).removeValue();
                                ref.child("Profile").child(user.getKey()).removeValue();
                                ref.child("Wallet").child(user.getKey()).removeValue();
                                ref.child("Cart").child(user.getKey()).removeValue();
                                ref.child("Favorite").child(user.getKey()).removeValue();
                                ref.child("Order").child(user.getKey()).removeValue();
                                ref.child("Payment").child(user.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        getActivity().finish();
                                        startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
            }
        });
    }
}