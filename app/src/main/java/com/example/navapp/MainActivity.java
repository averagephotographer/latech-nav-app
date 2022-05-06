package com.example.navapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView test;
    Uri uri;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_image_view);
        ImageView test = findViewById(R.id.testing);
        test.setImageResource(R.drawable.ic_baseline_add_24);
//        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(MainActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(90)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            uri = data.getData();
        try {
            test.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}