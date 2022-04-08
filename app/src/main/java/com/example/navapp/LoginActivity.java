package com.example.navapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    TextView dontHaveAcc;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userid;
    SharedPreferences sharedPreferences;
    public static final String Username = "username";
    public static final String pass_wrd = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        if(sharedPreferences.contains(Username)){
            Intent i = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(i);
        }

        dontHaveAcc = findViewById(R.id.dontHaveAcc);
        dontHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = mUsername.getText().toString();
                String passwordAAAA = mPassword.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Username is required!");
                    return;
                }
                if (TextUtils.isEmpty(passwordAAAA)) {
                    mPassword.setError("Password is required!");
                    return;
                }

                DocumentReference documentReference = fStore.collection("user_profile").document(username);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot DS = task.getResult();
                        String checkpw;
                        if (DS.exists()) {
                            checkpw = DS.get("password").toString();
                            if (BCrypt.checkpw(passwordAAAA, checkpw)) {
                                //Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Username, DS.get("username").toString());
                                //editor.putString(pass_wrd, DS.get("password").toString());
                                editor.commit();
<<<<<<< HEAD
                                startActivity(new Intent(getApplicationContext(), twofareg.class));
=======
                                firebaseAuth.signInWithEmailAndPassword(DS.get("email").toString(),passwordAAAA).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful())
                                        {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            if (user.isEmailVerified()) {
                                                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                                System.out.println("samuel 'dabuz' buzby");
                                                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                                            }
                                            else
                                            {
                                                Toast.makeText(LoginActivity.this, "Please Verify your email", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                                            System.out.println("no bitches?");

                                        }
                                    }
                                });
                                //startActivity(new Intent(getApplicationContext(), MapsActivity.class));
>>>>>>> parent of 70ab4ff (Revert "Added Email Verification to the app")
                            } else {
                                Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"No account found!", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

    }
}