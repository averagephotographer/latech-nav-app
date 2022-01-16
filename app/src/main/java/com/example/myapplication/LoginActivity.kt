package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var login : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login_btn)

        lifecycleScope.launch {
            while (true){
                login.isEnabled = !username.text.toString().equals("")
                delay(1)
            }
        }

        login.setOnClickListener{

            if(username.text.toString() == "Sadiat" && password.text.toString() == "Opeyemi"){
                // Toast.makeText(applicationContext, "you are logged in", Toast.LENGTH_LONG).show()
            }

            else{
                startActivity(Intent(this, MapActivity::class.java))
                // Toast.makeText(applicationContext, "incorrect password", Toast.LENGTH_LONG).show()

            }
        }


    }


}