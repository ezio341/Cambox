package com.example.cambox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cambox.fragment.CartFragment;
import com.example.cambox.fragment.FavoriteFragment;
import com.example.cambox.fragment.OrderFragment;
import com.example.cambox.fragment.ProductFragment;
import com.example.cambox.fragment.ProfileFragment;
import com.example.cambox.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private BottomNavigationView bottom_view;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        user = (User)getIntent().getParcelableExtra("user");
        Log.d("user key", user.getKey());
        getFragment(new ProductFragment(user));
        getSupportActionBar().hide();

        bottom_view = findViewById(R.id.bottom_view);
        bottom_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if(item.getItemId() == R.id.home){
                    getSupportActionBar().setTitle("Home");
                    getFragment(new ProductFragment(user));
                }else if(item.getItemId() == R.id.favorite) {
                    getSupportActionBar().setTitle("Favorite");
                    getFragment(new FavoriteFragment(user));
                }else if(item.getItemId() == R.id.order) {
                    getSupportActionBar().setTitle("Orders");
                    getFragment(new OrderFragment());
                }else if(item.getItemId() == R.id.cart) {
                    getSupportActionBar().setTitle("Cart");
                    getFragment(new CartFragment(user));
                }else if(item.getItemId() == R.id.profile) {
                    getSupportActionBar().setTitle("Profile");
                    getFragment(new ProfileFragment(user));
                }
                return false;
            }
        });

    }

    public User getUser() {
        return user;
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("user@gmail.com", "user12345").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getApplicationContext(), "client connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    private void getFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();
    }

    //    logout
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}
