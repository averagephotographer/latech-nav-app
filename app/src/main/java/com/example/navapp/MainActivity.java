package com.example.navapp;

import android.os.Bundle;

import com.example.navapp.databinding.ActivityDrawerBaseBinding;

public class MainActivity extends DrawerBaseActivity {

    ActivityDrawerBaseBinding activityDrawerBaseBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDrawerBaseBinding = ActivityDrawerBaseBinding.inflate(getLayoutInflater());
        setContentView(activityDrawerBaseBinding.getRoot());
    }
}
