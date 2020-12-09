package com.example.cambox.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cambox.R;
import com.example.cambox.adapter.CheckoutAdapter;
import com.example.cambox.databinding.FragmentCheckoutBinding;
import com.example.cambox.databinding.ItemCheckoutBinding;
import com.example.cambox.model.Cart;
import com.example.cambox.model.Courier;
import com.example.cambox.model.Order;
import com.example.cambox.model.Profile;
import com.example.cambox.model.User;
import com.example.cambox.model.Wallet;
import com.example.cambox.util.FormatUtil;
import com.example.cambox.util.FragmentUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class CheckoutFragment extends Fragment {
    FragmentCheckoutBinding binding;
    private static User user;
    private static List<Cart> cartList;
    private DatabaseReference ref;
    private ProgressDialog pg;

    public CheckoutFragment(User user, List<Cart> cartList) {
        this.user = user;
        this.cartList = cartList;
    }

    public CheckoutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = FirebaseDatabase.getInstance().getReference();
        pg = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false);
        binding.setCurrencyformat(FormatUtil.getCurrencyFormat());
        CheckoutAdapter adapter = new CheckoutAdapter(cartList);
        binding.rvCheckout.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCheckout.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, String> wmap = (HashMap) snapshot.child("Wallet").child(user.getKey()).getValue();
                Wallet wallet = new Wallet(Long.valueOf(wmap.get("balance")));
                HashMap<String, String> cmap = (HashMap) snapshot.child("Courier").child("0").getValue();
                Courier courier = new Courier(cmap.get("name"), Integer.valueOf(cmap.get("price")));
                Profile profile = snapshot.child("Profile").child(user.getKey()).getValue(Profile.class);
                binding.setProfile(profile);
                binding.setWallet(wallet);
                binding.setCourier(courier);
                int totalprice =0;
                for(Cart c : cartList){
                    int price = Integer.valueOf(snapshot.child("Item").child(c.getProduct()).child("price").getValue(String.class));
                    int pricetotal = price * c.getAmount();
                    totalprice += pricetotal;
                }
                binding.setTotalprice(totalprice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.btnBackCkt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FragmentUtil.getFragment(new CartFragment(user), getActivity());
            }
        });
        binding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new AlertDialog.Builder(getContext())
                .setTitle("Pay")
                .setMessage("Are You Sure to Continue The Payment?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pushOrder();
                    }
                })
                .setNegativeButton("No", null).show();
            }
        });
    }

    public void pushOrder(){
        pg.setMessage("Please Wait ...");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.show();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                int totalprice = 0;
                //count price total, decrease item stock
                for(Cart c : cartList){
                    int price = Integer.valueOf(snapshot.child("Item").child(c.getProduct()).child("price").getValue(String.class));
                    int pricetotal = price * c.getAmount();
                    totalprice += pricetotal;
                    String strstock = snapshot.child("Item").child(c.getProduct()).child("stock").getValue(String.class);
                    int stock = Integer.valueOf(strstock);
                    ref.child("Item").child(c.getProduct()).child("stock").setValue(String.valueOf(stock-c.getAmount()));
                }
                String orderkey = ref.child("Order").child(user.getKey()).push().getKey();
                String orderdate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                long orderlen = snapshot.child("Order").child(user.getKey()).getChildrenCount();
                HashMap<String, String> cmap = (HashMap) snapshot.child("Courier").child("0").getValue();
                final Courier courier = new Courier(cmap.get("name"), Integer.valueOf(cmap.get("price")));
                Order order= new Order(cartList, courier, "Processed", orderkey, orderdate, ((int) orderlen)+1, totalprice);
                final int finalTotalprice = totalprice;

                //push order to db, update wallet balance, remove cart items
                ref.child("Order").child(user.getKey()).child(orderkey).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String strbalance = snapshot.child("Wallet").child(user.getKey()).child("balance").getValue(String.class);
                        long balance = Long.valueOf(strbalance);
                        ref.child("Wallet").child(user.getKey()).child("balance").setValue(String.valueOf(balance - (finalTotalprice + courier.getPrice())));
                        ref.child("Cart").child(user.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getContext(), "Your Order are being Processed", Toast.LENGTH_SHORT).show();
                                FragmentUtil.getFragment(new CartFragment(user), getActivity());
                            }
                        });
                        pg.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}