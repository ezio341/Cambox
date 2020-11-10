package com.example.cambox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {

    LinearLayout btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (LinearLayout) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, Register.class);
                startActivity(intent);
            }
        });

    }
}