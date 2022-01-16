package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SignOptionActivity : AppCompatActivity() {
    private lateinit var signin_btn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_option)
        signin_btn = findViewById(R.id.sign_in)

        signin_btn.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}