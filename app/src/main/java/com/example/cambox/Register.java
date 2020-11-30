package com.example.cambox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.cambox.databinding.ActivityRegisterBinding;
import com.example.cambox.model.Profile;
import com.example.cambox.model.User;
import com.example.cambox.util.SecurityUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private DatabaseReference ref;
    private User user;

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
                String email = binding.mRegEmail.getText().toString().trim();
                String password = binding.mRegPassword.getText().toString().trim();
                String uname = binding.mUsername.getText().toString();

                if(TextUtils.isEmpty(email) ){
                    binding.mRegEmail.setError("Email is Required");
                }

                if(TextUtils.isEmpty(uname)){
                    binding.mUsername.setError("Username is Required");
                }

                if(TextUtils.isEmpty(password)){
                    binding.mRegPassword.setError("Password is Required");
                }else if(password.length() < 6){
                    binding.mRegPassword.setError("Password must be more 6 character");
                }

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(uname) && !TextUtils.isEmpty(password) && !(password.length() < 6)){
                    final ProgressDialog pg = new ProgressDialog(Register.this);
                    pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pg.setTitle("Registering");
                    pg.setMessage("Please Wait ...");
                    pg.show();
                    // REGISTER TO FIREBASE
                    user = new User(binding.mRegEmail.getText().toString(), binding.mUsername.getText().toString()
                            , binding.mRegPassword.getText().toString(), binding.mPhone.getText().toString());
                    final Profile p = new Profile(binding.mUsername.getText().toString());
                    user.setKey(SecurityUtil.digest(binding.mRegEmail.getText().toString()));

                    ref.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child(SecurityUtil.digest(binding.mRegEmail.getText().toString())).exists()){
                                binding.mRegEmail.setError("Email already exist");
                                pg.dismiss();
                                return;
                            }else{
                                ref.child("Account").child(SecurityUtil.digest(binding.mRegEmail.getText().toString())).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            ref.child("Profile").child(user.getKey()).setValue(p);
                                            Intent intent = new Intent(Register.this, MainActivity.class);
                                            intent.putExtra("user", user);
                                            pg.dismiss();
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Register.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

       binding.linkLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               TextView txt = (TextView) view;
               txt.setTextColor(Color.BLUE);
               startActivity(new Intent(getApplicationContext(), Login.class));
               finish();
           }
       });

    }
}