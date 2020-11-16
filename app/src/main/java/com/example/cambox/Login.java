package com.example.cambox;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cambox.model.User;
import com.example.cambox.Util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mlogin;
    TextView mRegister;
    FirebaseDatabase fAuth;
    DatabaseReference ref;
    ProgressBar progressBar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.mEmail);
        mPassword = findViewById(R.id.mPassword);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseDatabase.getInstance();
        ref = fAuth.getReference();
        mlogin = findViewById(R.id.btnLogin);
        mRegister = findViewById(R.id.createText);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mEmail.setError("Email is Required");
                    return;
                }

//                progressBar.setVisibility(View.VISIBLE);

                user = new User(mEmail.getText().toString(), mPassword.getText().toString());
                //Authenticate user
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //variable to store email and password
                        String emaildb = "";
                        String passdb = "";
                        boolean valid=false;
                        DataSnapshot data = snapshot.child("Account").child(Util.digest(user.getEmail()));
                        //get encrypted email from key value of user.
                        emaildb = data.getKey();
                        //get the password value
                        passdb = data.child("password").getValue(String.class);
                        try {
                            if (emaildb.equals(Util.digest(user.getEmail())) && passdb.equals(user.getPassword())) {
                                user.setKey(data.getKey());
                                user.setName(data.child("name").getValue(String.class));
                                user.setPhone(data.child("phone").getValue(String.class));
                                Toast.makeText(Login.this, "Login Successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("user",user);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }catch (NullPointerException e){
                            Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }
}