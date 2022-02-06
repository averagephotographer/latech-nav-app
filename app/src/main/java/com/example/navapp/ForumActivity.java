package com.example.navapp;

import android.os.Bundle;

import com.example.navapp.databinding.ActivityDrawerBaseBinding;
import com.example.navapp.databinding.ActivityForumBinding;

public class ForumActivity extends DrawerBaseActivity {
    ActivityForumBinding activityForumBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForumBinding = ActivityForumBinding.inflate(getLayoutInflater());
        setContentView(activityForumBinding.getRoot());
        allocateActivityTitle("Forum");
    }

}
