package com.example.navapp;

import static com.example.navapp.RegisterActivity.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.navapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    StorageReference postImageRef;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser mUser;
    Uri imageUri;

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
        postImageRef = FirebaseStorage.getInstance().getReference().child("images");

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            imageUri = data.getData();
                            postImage.setImageURI(imageUri);
                        }
                    }
                });
        postImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //Create Intent
                 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                 intent.setType("image/*");
                 intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                 someActivityResultLauncher.launch(intent);
             }
        });



        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = write_title.getText().toString().trim();
                String description = user_input.getText().toString().trim();

                sharedPreferences = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String name = sharedPreferences.getString("username", "");

                Map<String,Object> posts = new HashMap<>();

                if(TextUtils.isEmpty(title)) {
                    Toast.makeText(CreatePostActivity.this, "Please write a title before posting!", Toast.LENGTH_LONG).show();
                }
                else {
                    postImageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                postImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Date date = new Date();
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                                        String strDate = formatter.format(date);
                                        posts.put("title", title);
                                        posts.put("description", description);
                                        posts.put("imageURL", uri.toString());
                                        posts.put("datePost", strDate);

                                    }
                                });
                            }
                        }
                    });
                    DocumentReference documentReference = firestore.collection("posts").document(name).collection("myposts").document(title);
                    documentReference.set(posts).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CreatePostActivity.this, "Posted!", Toast.LENGTH_LONG).show();
                        }
                    });
                    startActivity(new Intent(getApplicationContext(),ForumActivity.class));
                }
            }});

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
