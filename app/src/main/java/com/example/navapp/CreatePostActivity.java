package com.example.navapp;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {
    TextView create_post;
    EditText user_input;
    EditText write_title;
    ImageView postImage;
    SharedPreferences sharedPreferences;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    Uri imageUri;
    String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        create_post = findViewById(R.id.post);
        write_title = findViewById(R.id.writeTitle);
        // user_input is basically the description or the body of the post
        user_input = findViewById(R.id.user_input);
        postImage = findViewById(R.id.postImage);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        current_user = firebaseAuth.getCurrentUser().getUid();

        postImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ImagePicker.with(CreatePostActivity.this)
                         .crop()	    			//Crop image(Optional), Check Customization for more option
                         .compress(1024)			//Final image size will be less than 1 MB(Optional)
                         .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                         .start();
             }
        });


        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = write_title.getText().toString().trim();
                String description = user_input.getText().toString().trim();


                sharedPreferences = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String name = sharedPreferences.getString("username", "");
                storageReference = FirebaseStorage.getInstance().getReference();

                Map<String,Object> posts = new HashMap<>();
                posts.put("username", name);
                posts.put("title", title);
                posts.put("description", description);
                posts.put("uid", current_user);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                String strDate = formatter.format(date);
                posts.put("datePost", strDate);

                if(TextUtils.isEmpty(title)) {
                    Toast.makeText(CreatePostActivity.this, "Please write a title before posting!", Toast.LENGTH_LONG).show();
                }
                else {
                    if (imageUri != null) {
                        StorageReference postRef = storageReference.child("post_images").child(FieldValue.serverTimestamp().toString() + "jpg");
                        postRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        posts.put("imageURL", uri.toString());
                                        firestore.collection("posts").add(posts).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(CreatePostActivity.this, "Posted!", Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });

                                    }
                                });
                            }
                        });
                    }
                    else {
                        firestore.collection("posts").add(posts).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CreatePostActivity.this, "Posted!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                    //Task<DocumentReference> documentReference = firestore.collection("posts").document(name).collection("myposts").add(posts)
                    startActivity(new Intent(getApplicationContext(),ForumActivity.class));


                    //startActivity(new Intent(getApplicationContext(),ForumActivity.class));
                }
            }});

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            imageUri = data.getData();
            postImage.setImageURI(imageUri);
        }
    }


    @Override
        public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Not Creating a Post?")
                    .setMessage("Are you sure you want to exit? Post will not be saved!")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }


    }
