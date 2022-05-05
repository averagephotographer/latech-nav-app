package com.example.navapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.OAuthProvider;

public class SignOptionsActivity extends AppCompatActivity {
    private Button signIn_btn;
    private Button signUp_btn;
    private Button twt;
    SharedPreferences sharedPreferences;
    public static final String Username = "username";
    public static final String pass_wrd = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_options);

        signIn_btn = findViewById(R.id.sign_in);
        signUp_btn = findViewById(R.id.sign_up);

        twt = findViewById(R.id.sign_inwithtwitter);

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        if(sharedPreferences.contains(Username)){
            Intent i = new Intent(SignOptionsActivity.this, MapsActivity.class);
            startActivity(i);
        }

        twt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignOptionsActivity.this, TwitterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });

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