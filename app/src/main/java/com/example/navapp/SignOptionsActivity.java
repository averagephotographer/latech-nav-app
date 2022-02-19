package com.example.navapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignOptionsActivity extends AppCompatActivity {
    private Button signIn_btn;
    private Button signUp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_options);

        signIn_btn = findViewById(R.id.sign_in);
        signUp_btn = findViewById(R.id.sign_up);

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignOptionsActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignOptionsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}