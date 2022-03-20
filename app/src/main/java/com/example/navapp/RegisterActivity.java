package com.example.navapp;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
/*import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;*/
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Result;

import org.mindrot.jbcrypt.*;



public class RegisterActivity extends AppCompatActivity implements TextWatcher {
    private TextView haveAccount_text;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    ProgressBar progressBarInBackground;
    String userID;
    public static final String TAG = "TAG";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        db = FirebaseFirestore.getInstance();
        db.setLoggingEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        progressBarInBackground = findViewById(R.id.progressBarLoading);

        haveAccount_text = (TextView) findViewById(R.id.haveAccount);
        haveAccount_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        EditText email = (EditText)findViewById(R.id.inputEmail);
        EditText input_username = (EditText)findViewById(R.id.inputUsername);
        EditText password = (EditText)findViewById(R.id.inputPassword);
        EditText cpassword = (EditText)findViewById(R.id.inputCPassword);
        Button loginNow = (Button) findViewById(R.id.buttonRegister);
        password.addTextChangedListener(this);
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_str = email.getText().toString();
                String username_str = input_username.getText().toString();
                String password_str = password.getText().toString();

                Map<String,Object> user = new HashMap<>();
                user.put("username", username_str);
                user.put("email", email_str);

                if(TextUtils.isEmpty(email_str)) {
                    email.setError("Email is required!");
                    return;
                }

                if(TextUtils.isEmpty(username_str)) {
                    input_username.setError("Username is required!");
                    return;
                }

                if(TextUtils.isEmpty(password_str)) {
                    password.setError("Password is required!");
                    return;
                }

                if(!isValidPassword(password_str)) {
                    Toast.makeText(RegisterActivity.this,"Please input valid combination of password!",Toast.LENGTH_LONG).show();
                }

                else if((!password_str.equals(cpassword.getText().toString()))){
                    Toast.makeText(RegisterActivity.this, "Confirm Password Not Matched", Toast.LENGTH_SHORT).show();
                }

                // register user into firebase
                else  {
                    String hashpwstring = BCrypt.hashpw(password_str,BCrypt.gensalt(18));
                    user.put("password", hashpwstring);
                    progressBarInBackground.setVisibility(View.VISIBLE);
                    fAuth.createUserWithEmailAndPassword(email_str, password_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = db.collection("user_profile").document(userID);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user profile is created for " + userID);
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressBarInBackground.setVisibility(View.GONE);

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(
            CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updatePasswordStrengthView(s.toString());
    }

    private void updatePasswordStrengthView(String password) {

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView strengthView = (TextView) findViewById(R.id.password_strength);
        if (TextView.VISIBLE != strengthView.getVisibility())
            return;

        if (password.isEmpty()) {
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }
        if (isValidPassword(password)){
            progressBar.setProgress(100);
            strengthView.setText("Strong");
        }
        else {
            progressBar.setProgress(0);
            strengthView.setText("Weak");
        }
    }
    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[!?@#$%^&*()+=,.])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}



