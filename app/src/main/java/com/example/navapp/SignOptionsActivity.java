package com.example.navapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignOptionsActivity extends AppCompatActivity {
    private Button signin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_options);

        signin_btn = findViewById(R.id.sign_in);
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignOptionsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}