package com.example.navapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TwitterActivity extends RegisterActivity {
    private FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        OAuthProvider.Builder twt = OAuthProvider.newBuilder("twitter.com");
        twt.addCustomParameter("lang", "en");
        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                    Map<String,Object> user = new HashMap<>();
                                    final String username = authResult.getAdditionalUserInfo().getUsername();
                                    final String email = authResult.getUser().getEmail();


                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", username);
                                    //editor.putString(pass_wrd, DS.get("password").toString());
                                    editor.commit();
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));


                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Toast.makeText(TwitterActivity.this, e.getMessage(),Toast.LENGTH_LONG);
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
            firebaseAuth
                    .startActivityForSignInWithProvider(/* activity= */ this, twt.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                    final String username = authResult.getAdditionalUserInfo().getUsername().toString();
                                   // final String email = authResult.getUser().getEmail();
                                    Map<String,Object> user = new HashMap<>();
                                    //System.out.println(email);

                                    user.put("username", username);
                                    if (db.collection("user_profile").document(username) == null)
                                    {
                                        db.collection("user_profile").document(username).set(user);
                                    }
                                    ;
                                    //user.put("email", email);
                                   /* DocumentReference documentReference = db.collection("user_profile").document(username);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: user profile is created for ");
                                        }
                                    });;/**/

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", username);
                                    //editor.putString(pass_wrd, DS.get("password").toString());
                                    editor.commit();
                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    System.out.println(e.getMessage());
                                }
                            });
        }

    }
}