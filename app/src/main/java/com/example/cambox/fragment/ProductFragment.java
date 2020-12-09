package com.example.cambox.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.cambox.R;
import com.example.cambox.adapter.ProductAdapter;
import com.example.cambox.databinding.FragmentProductBinding;
import com.example.cambox.interfaces.OnClickListenerProduct;
import com.example.cambox.model.Product;
import com.example.cambox.model.User;
import com.example.cambox.util.FragmentUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProductFragment extends Fragment {
    private FragmentProductBinding binding;
    private static User user;

    private DatabaseReference dbRef;

    private ProgressDialog pg;
    private List<Product> list;

    public ProductFragment(User user) {
        this.user = user;
    }

    public ProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbRef = FirebaseDatabase.getInstance().getReference();
        pg = new ProgressDialog(getContext());
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pg.setMessage("Loading ...");
        pg.show();
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //firebase single listener
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.child("Item").getChildren()){
                    final HashMap<String, String> p= (HashMap) data.getValue();
                        Product products = new Product(p.get("name"), p.get("desc"), p.get("img"), Integer.valueOf(p.get("price")), p.get("product_date"),
                                Integer.valueOf(p.get("stock")), Double.valueOf(p.get("disc")), p.get("key"));
                        list.add(products);
                }
                binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                final ProductAdapter adapter = new ProductAdapter(list, user);

                adapter.setListener(getListener());
                binding.recyclerView.setAdapter(adapter);
                pg.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


    //Onclick listener for each item
    public OnClickListenerProduct getListener(){

        return new OnClickListenerProduct() {
            @Override
            public void onclick(Product product) {
                ViewProductFragment fragment = new ViewProductFragment(product, ProductFragment.this);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                fragment.setArguments(bundle);
                FragmentUtil.getFragment(fragment, getActivity());
            }

            @Override
            public void onClickFavorite(final Product product, final ImageButton btn) {
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Favorite").child(user.getKey()).child(product.getKey()).exists()){
                            dbRef.child("Favorite").child(user.getKey()).child(product.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                }
                            });
                        }else{
                            dbRef.child("Favorite").child(user.getKey()).child(product.getKey()).setValue(product.getKey()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    btn.setImageResource(R.drawable.ic_baseline_favorite_pink_24);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
    }

}