package com.example.navapp;

import static android.content.ContentValues.TAG;
import static com.example.navapp.RegisterActivity.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    TextView username, email;
    Button log_out;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    //String userID;
    SharedPreferences sharedPreferences;
    public static final String Username = "username";
    public static final String pass_wrd = "password";
    private CircleImageView circleImageView;
    private StorageReference storageReference;
    private Uri mImageUri;
    private Button saveProfilePic;
    private Boolean isPhotoSelected;
    public String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        username = findViewById(R.id.profilename);
        email = findViewById(R.id.profileemail);
        log_out = findViewById(R.id.sign_out);
        circleImageView = findViewById(R.id.circleImageView);
        saveProfilePic = findViewById(R.id.savePic);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        userID = fAuth.getCurrentUser().getUid();
        sharedPreferences = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        //sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        if (!(sharedPreferences.contains(Username))) {
            Intent i = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(i);
        }
         String name = sharedPreferences.getString("username", "");

        DocumentReference documentReference = fStore.collection("user_profile").document(name);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                username.setText(documentSnapshot.getString("username"));
                email.setText(documentSnapshot.getString("email"));
            }
        });

        circleImageView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AccountActivity.this)
                        .galleryOnly()
                        .crop()                 //Crop image(Optional), Check Customization for more option
                        .cropSquare()
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        saveProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference reference = FirebaseStorage.getInstance().getReference("profile_picture/" + name + ".jpg");
                if (isPhotoSelected) {
                    if (mImageUri != null) {
                        reference.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            saveToFireStore(task, uri);
                                        }
                                    });
                                }
                            }
                        });
                    }
                } else {
                    saveToFireStore(null, mImageUri);
                }
            }

        });

        fStore.collection("user_profile").document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists() && mImageUri != null){
                        String imageUrl = task.getResult().getString("profilePicURL");
                        mImageUri = Uri.parse(imageUrl);

                        Glide.with(AccountActivity.this).load(imageUrl).into(circleImageView);
                    }
                    else {
                        Glide.with(AccountActivity.this).load(R.drawable.addimage).into(circleImageView);
                    }
                }
            }
        });


        log_out.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                sharedPreferences = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.commit();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }
        });
    }

    private void saveToFireStore(Task<UploadTask.TaskSnapshot> task, Uri downloadUri) {
        String name = sharedPreferences.getString("username", "");
        user.put("profilePicURL", downloadUri.toString());
        DocumentReference documentReference = fStore.collection("user_profile").document(name);
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AccountActivity.this, "Uploaded Complete.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mImageUri = data.getData();
        circleImageView.setImageURI(mImageUri);

        isPhotoSelected = true;
    }

}
