package com.example.cambox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.cambox.Fragment.CartFragment;
import com.example.cambox.Fragment.FavoriteFragment;
import com.example.cambox.Fragment.HomeFragment;
import com.example.cambox.Fragment.OrderFragment;
import com.example.cambox.Fragment.ProductFragment;
import com.example.cambox.Fragment.ProfileFragment;
import com.example.cambox.databinding.FragmentProfileBinding;
import com.example.cambox.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottom_view;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        user = (User)getIntent().getParcelableExtra("user");
        getFragment(new ProductFragment());

        bottom_view = findViewById(R.id.bottom_view);
        bottom_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    getSupportActionBar().setTitle("Home");
                    getFragment(new ProductFragment());
                }else if(item.getItemId() == R.id.favorite) {
                    getSupportActionBar().setTitle("Favorite");
                    getFragment(new FavoriteFragment());
                }else if(item.getItemId() == R.id.order) {
                    getSupportActionBar().setTitle("Orders");
                    getFragment(new OrderFragment());
                }else if(item.getItemId() == R.id.cart) {
                    getSupportActionBar().setTitle("Cart");
                    getFragment(new CartFragment());
                }else if(item.getItemId() == R.id.profile) {
                    getSupportActionBar().setTitle("Profile");
                    getFragment(new ProfileFragment(user));
                }
                return false;
            }
        });
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
