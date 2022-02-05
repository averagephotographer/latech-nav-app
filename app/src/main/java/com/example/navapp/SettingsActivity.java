package com.example.navapp;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.android.material.switchmaterial.SwitchMaterial;


public class SettingsActivity extends AppCompatActivity {
    //Initialize variable
    SwitchMaterial switch_btn;
    ImageView about;

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
        setContentView(R.layout.activity_settings);

        // assign variables

        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        about = findViewById(R.id.right_arrow2);
        switch_btn = findViewById(R.id.switch_mode);


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //check condition
                if (isChecked){
                    //when switch button is checked
                    //Set night mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }else{
                    //When switch button is unchecked
                    //Set light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });


    }
}