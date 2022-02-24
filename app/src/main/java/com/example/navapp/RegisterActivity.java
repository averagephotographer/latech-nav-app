package com.example.navapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity implements TextWatcher {
    private TextView haveAccount_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        haveAccount_text = (TextView) findViewById(R.id.haveAccount);
        haveAccount_text.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            }
        });

        EditText password = (EditText)findViewById(R.id.inputPassword);
        EditText cpassword = (EditText)findViewById(R.id.inputCPassword);
        Button loginNow = (Button) findViewById(R.id.buttonRegister);
        password.addTextChangedListener(this);
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isValidPassword(password.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"Please input valid combination of password!",Toast.LENGTH_LONG).show();
                } else if((!password.getText().toString().equals(cpassword.getText().toString()))){
                    Toast.makeText(RegisterActivity.this, "Confirm Password Not Matched", Toast.LENGTH_SHORT).show();
                } else if((password.getText().toString().equals(cpassword.getText().toString()))) {
                    Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
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
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
