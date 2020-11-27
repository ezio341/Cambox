package com.example.cambox.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.cambox.R;
import com.example.cambox.databinding.FragmentEditProfileBinding;
import com.example.cambox.model.Profile;
import com.example.cambox.model.User;
import com.example.cambox.util.FragmentUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class EditProfileFragment extends Fragment {
    private FragmentEditProfileBinding binding;
    private User user;
    private DatabaseReference ref;

    public EditProfileFragment(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile p = snapshot.child("Profile").child(user.getKey()).getValue(Profile.class);
                binding.setProfile(p);
                if(p.getGender().equals("Male") || p.getGender().isEmpty()){
                    binding.rbMale.setChecked(true);
                }else{
                    binding.rbFemale.setChecked(true);
                }
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
        binding.mDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.mDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        binding.btnEditProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.getFragment(new ProfileFragment(user), getActivity());
            }
        });
        binding.rbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.rbMale.setChecked(true);
                binding.rbFemale.setChecked(false);
            }
        });
        binding.rbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.rbMale.setChecked(false);
                binding.rbFemale.setChecked(true);
            }
        });
        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.mName.getText().toString();
                String address = binding.mAddress.getText().toString();
                String dob = binding.mDob.getText().toString();
                String gender = "";
                if(binding.rbMale.isChecked()){
                    gender = binding.rbMale.getText().toString();
                }else{
                    gender = binding.rbFemale.getText().toString();
                }
                final Profile p = new Profile(name, address, dob, gender);

                final ProgressDialog pg = new ProgressDialog(getContext());
                pg.setTitle("Updating");
                pg.setMessage("Please wait ...");
                pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pg.show();
                ref.child("Profile").child(user.getKey()).setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FragmentUtil.getFragment(new ProfileFragment(user), getActivity());
                            Toast.makeText(getContext(), "Your profile has been updated!", Toast.LENGTH_SHORT);
                            pg.dismiss();
                        }
                    }
                });
            }
        });
    }
}