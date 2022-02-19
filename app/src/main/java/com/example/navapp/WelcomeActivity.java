package com.example.navapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;


public class WelcomeActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // associating the layout with this file
        setContentView(R.layout.activity_welcome);

        // setting up button from activity_welcome
        Button continue_button = findViewById(R.id.btn_continue);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignOptionsActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }
}
