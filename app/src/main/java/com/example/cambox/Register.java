package com.example.cambox;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cambox.model.User;
import com.example.cambox.Util.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText mUsername,mEmail,mPassword;
    Button btnRegister;
    TextView mLogin;
    FirebaseDatabase fAuth;
    DatabaseReference ref;
    ProgressBar progressBar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.mUsername);
        mEmail = findViewById(R.id.mEmail);
        mPassword = findViewById(R.id.mPassword);
        btnRegister = findViewById(R.id.btnRegister);
        mLogin = findViewById(R.id.createText);

        fAuth = FirebaseDatabase.getInstance();
        ref = fAuth.getReference();
        progressBar = findViewById(R.id.progressBar);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    mEmail.setError("Email is Required");
                    return;
                }
                else if(password.length() < 6){
                    mPassword.setError("Password must be more 6 character");
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);

                    // REGISTER TO FIREBASE
                    user = new User(mEmail.getText().toString(), mPassword.getText().toString());
                    ref.child("Account").child(Util.digest(mEmail.getText().toString())).setValue(user);
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }
            }
        });

       mLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), Login.class));
           }
       });

    }
}