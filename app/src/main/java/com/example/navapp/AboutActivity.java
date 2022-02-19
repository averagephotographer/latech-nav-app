package com.example.navapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Check condition
        if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES){
            //When night mode is equal to yes
            //Set dark theme
            setTheme(R.style.Theme_NavApp_Night);
        }else{
            //When night mode is equal to no
            //set light theme
            setTheme(R.style.ThemeNavApp_Light);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

}