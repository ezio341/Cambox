package com.example.cambox;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.cambox.databinding.ActivityRegisterBinding;
import com.example.cambox.model.User;
import com.example.cambox.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    ActivityRegisterBinding binding;
    DatabaseReference ref;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        ref = FirebaseDatabase.getInstance().getReference();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.mEmail.getText().toString().trim();
                String password = binding.mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    binding.mEmail.setError("Email is Required");
                    return;
                }
                else if(password.length() < 6){
                    binding.mPassword.setError("Password must be more 6 character");
                }
                else{
                    binding.progressBar.setVisibility(View.VISIBLE);

                    // REGISTER TO FIREBASE
                    user = new User(binding.mEmail.getText().toString(), binding.mPassword.getText().toString());
                    ref.child("Account").child(Util.digest(binding.mEmail.getText().toString())).setValue(user);
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            }
        });

       binding.createText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), Login.class));
           }
       });

    }
}