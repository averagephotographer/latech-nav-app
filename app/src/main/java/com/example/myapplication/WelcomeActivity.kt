package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WelcomeActivity : AppCompatActivity() {
    private lateinit var continue_btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        continue_btn = findViewById(R.id.btn_continue)


        continue_btn.setOnClickListener{
            startActivity(Intent(this, SignOptionActivity::class.java))
        }
    }
}