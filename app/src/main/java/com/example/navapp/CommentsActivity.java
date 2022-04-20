package com.example.navapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    private EditText comment_post;
    private ImageView add_comment;
    private RecyclerView recycle;
    private FirebaseFirestore firestore;
    private String post_id;
    private CommentsAdapter adapter;
    private List<Comments> commList;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        comment_post = findViewById(R.id.comment);
        add_comment = findViewById(R.id.send);
        recycle = findViewById(R.id.comment_recyclerView);

        firestore = FirebaseFirestore.getInstance();

        commList = new ArrayList<>();
        adapter = new CommentsAdapter(CommentsActivity.this , commList);

        post_id = getIntent().getStringExtra("postid");

        sharedpreferences = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String name = sharedpreferences.getString("username", "");


        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(adapter);


        firestore.collection("posts/" + post_id + "/comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){

                        Comments comments = documentChange.getDocument().toObject(Comments.class);
                        commList.add(comments);
                        adapter.notifyDataSetChanged();

                    }else{
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });


        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_comment = comment_post.getText().toString();
                if(!user_comment.isEmpty()){
                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("username", name);
                    commentsMap.put("comment", user_comment);
                    commentsMap.put("time", FieldValue.serverTimestamp());
                    firestore.collection("posts/" + post_id + "/comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(CommentsActivity.this,"Comment Added", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(CommentsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(CommentsActivity.this, "Please write a comment", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}