package com.example.navapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.navapp.Utils.Posts;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ImageViewActivity extends AppCompatActivity {

    private PhotoView test;
    private FirebaseFirestore firestore;
    private String post_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_image_view);

        firestore = FirebaseFirestore.getInstance();
        post_id = getIntent().getStringExtra("postid");



        firestore.collection("posts").document(post_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String postImage = task.getResult().getString("imageURL");
                    // check if user has a post picture
                    if (postImage != null) {
                        setProfilePic(postImage);
                    }
                }
                else {
                    Toast.makeText(ImageViewActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setProfilePic(String urlProfile){
        test = findViewById(R.id.testing);
        Glide.with(ImageViewActivity.this).load(urlProfile).into(test);
    }
}
