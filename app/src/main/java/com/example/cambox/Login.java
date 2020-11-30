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

import com.example.cambox.databinding.ActivityLoginBinding;
import com.example.cambox.model.User;
import com.example.cambox.util.SecurityUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private DatabaseReference ref;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        ref = FirebaseDatabase.getInstance().getReference();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pg = new ProgressDialog(Login.this);
                pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pg.setTitle("Logging in");
                pg.setMessage("Please Wait ...");

                String email = binding.mEmail.getText().toString().trim();
                String password = binding.mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    binding.mEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    binding.mPassword.setError("Password is Required");
                    return;
                }

                pg.show();
                user = new User(binding.mEmail.getText().toString(), binding.mPassword.getText().toString());
                //Authenticate user
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //variable to store email and password
                        String emaildb = "";
                        String passdb = "";
                        DataSnapshot data = snapshot.child("Account").child(SecurityUtil.digest(user.getEmail()));
                        //get encrypted email from key value of user.
                        emaildb = data.getKey();
                        //get the password value
                        passdb = data.child("password").getValue(String.class);
                        try {
                            if (emaildb.equals(SecurityUtil.digest(user.getEmail())) && passdb.equals(user.getPassword())) {
                                user.setKey(data.getKey());
                                user.setName(data.child("name").getValue(String.class));
                                user.setPhone(data.child("phone").getValue(String.class));
                                Toast.makeText(Login.this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("user",user);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }catch (NullPointerException e){
                            Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                        pg.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        pg.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
            }
        });


        binding.linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txt = (TextView) view;
                txt.setTextColor(Color.BLUE);
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });
    }
}