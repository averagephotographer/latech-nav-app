package com.example.navapp;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

<<<<<<< Updated upstream
=======
import javax.xml.transform.Result;

import org.mindrot.jbcrypt.*;

>>>>>>> Stashed changes

public class RegisterActivity extends AppCompatActivity implements TextWatcher {
    private TextView haveAccount_text;

    public static final String DATABASE_NAME = "nav-app";
    public static final String url = "jdbc:mysql://nav-app.czayyymumdfr.us-east-1.rds.amazonaws.com:3306/nav-app";
    public static final String username = "admin", password="latechbulldog";

    public static final String TABLE_NAME = "user_profile";




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

                if(!isValidPassword(password_str)){
                    Toast.makeText(RegisterActivity.this,"Please input valid combination of password!",Toast.LENGTH_LONG).show();
                } else if((!password_str.equals(cpassword.getText().toString()))){
                    Toast.makeText(RegisterActivity.this, "Confirm Password Not Matched", Toast.LENGTH_SHORT).show();
<<<<<<< Updated upstream
                } else {
                    //Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
=======
                } else  {
<<<<<<< HEAD
                    //Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    new Task().execute();
=======
>>>>>>> Stashed changes
                    addTemp(username_str, password_str, email_str);
>>>>>>> 9f26046295797d0362e44a1f0b3f2843f169890a
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


<<<<<<< Updated upstream
    public void addTemp(String Username, String hashpassword, String Emaillocalname) {
        Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

        /*
        new Thread(() -> {
            Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
        */
        try {

               Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                /*
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
=======
<<<<<<< HEAD
    //public static void addTemp(String Username, String hpassword, String Email) {
    class Task extends AsyncTask<Void, Void, Void>{
        String records = "";
        String error = "";
=======
    public void addTemp(String Username, String hashpassword, String Emaillocalname) {
>>>>>>> 9f26046295797d0362e44a1f0b3f2843f169890a

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM user_profile");
                while (resultSet.next()) {
                    records += resultSet.getString(1) + " " + resultSet.getString(2);

<<<<<<< HEAD
                }
            }
            catch(Exception e){
                error = e.toString();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid){
            Toast.makeText(RegisterActivity.this, records, Toast.LENGTH_SHORT).show();
            if(error != "")
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }




}

            /*
            //char[] hashed = BCrypt.withDefaults().hashToChar(12,hashpassword.toCharArray());
            //String[] emailparts = Emaillocalname.split("@");
            emailparts[1] = "@" + emailparts[1];
=======
        String hashed = BCrypt.hashpw(hashpassword,BCrypt.gensalt(14));
        Toast.makeText(RegisterActivity.this, hashed, Toast.LENGTH_SHORT).show();

        String[] emailparts = Emaillocalname.split("@");
        emailparts[1] = "@" + emailparts[1];
>>>>>>> 9f26046295797d0362e44a1f0b3f2843f169890a
            Random r = new Random();
            int uid = r.nextInt(899999999)+100000000;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet RS;
                RS = statement.executeQuery("select domainid from domainlist where domainname ="+ emailparts[1] + ";");
                if (RS.first() == false)
                {
                    statement.executeQuery("insert into domainlist (domainname) values ("+emailparts[1]+");");
                    RS = statement.executeQuery("select domainid from domainlist where domainname ="+ emailparts[1] + ";");
                    Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                }
                String domid = RS.getString("DomainId");
                //Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
>>>>>>> Stashed changes

                statement.execute("INSERT INTO " + TABLE_NAME + "(username, hashpassword, Emaillocalname) VALUES('" + Username + "', '" + hashpassword + "', '" + Emaillocalname + "')");

                connection.close();
                */

                Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

            }
        catch (Exception e) {
                e.printStackTrace();
            }
            */

<<<<<<< Updated upstream
        //}).start();




    }
}
=======
//}
>>>>>>> Stashed changes

