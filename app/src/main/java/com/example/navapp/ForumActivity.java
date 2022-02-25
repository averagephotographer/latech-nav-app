package com.example.navapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.navapp.databinding.ActivityDrawerBaseBinding;
import com.example.navapp.databinding.ActivityForumBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ForumActivity extends DrawerBaseActivity {
    ActivityForumBinding activityForumBinding;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForumBinding = ActivityForumBinding.inflate(getLayoutInflater());
        setContentView(activityForumBinding.getRoot());
        allocateActivityTitle("Forum");

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForumActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });
    }

}
