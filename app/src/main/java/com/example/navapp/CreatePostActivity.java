package com.example.navapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreatePostActivity extends AppCompatActivity {
    TextView create_post;
    EditText user_input;
    EditText write_title;
    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        create_post=(TextView) findViewById(R.id.post);
        write_title = findViewById(R.id.writeTitle);

        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string = write_title.getText().toString();
                Intent intent = new Intent(CreatePostActivity.this, ForumActivity.class);
                intent.putExtra("Value", string);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to exit? Post will not be saved!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
