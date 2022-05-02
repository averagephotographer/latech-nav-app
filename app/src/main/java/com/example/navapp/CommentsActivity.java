package com.example.navapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navapp.Utils.Comments;
import com.example.navapp.Utils.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    private EditText comment_post;
    private TextView comm_num;
    private ImageView add_comment;
    private RecyclerView recycle;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String post_id;
    private CommentsAdapter adapter;
    private List<Comments> commList;
    private List<Users> usersList;
    boolean processComm = false;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        comment_post = findViewById(R.id.comment);
        add_comment = findViewById(R.id.send);
        recycle = findViewById(R.id.comment_recyclerView);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        commList = new ArrayList<>();
        usersList = new ArrayList<>();
        adapter = new CommentsAdapter(CommentsActivity.this , commList, usersList);

        post_id = getIntent().getStringExtra("postid");

        sharedpreferences = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String name = sharedpreferences.getString("username", "");


        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(adapter);


        firestore.collection("posts/" + post_id + "/comments").addSnapshotListener(CommentsActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){

                        Comments comments = documentChange.getDocument().toObject(Comments.class);
                        String user = documentChange.getDocument().getString("username");
                        firestore.collection("user_profile").document(user).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            Users users = task.getResult().toObject(Users.class);
                                            usersList.add(users);
                                            commList.add(comments);
                                            adapter.notifyDataSetChanged();
                                        }else{
                                            Toast.makeText(CommentsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

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
                    commentsMap.put("uid", uid);
                    firestore.collection("posts/" + post_id + "/comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(CommentsActivity.this,"Comment Added", Toast.LENGTH_SHORT).show();
                                System.out.println("hi");
                                updateCommentCount(commentsMap);
                                adapter.notifyDataSetChanged();
                                comment_post.getText().clear();
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

    private void updateCommentCount(Map commentsMap) {
        processComm = true;
        DocumentReference ref = (DocumentReference) FirebaseFirestore.getInstance().collection("posts").document(post_id);
                ref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(CommentsActivity.this, "Error", Toast.LENGTH_SHORT);
                    return;
                }

                if (processComm == true && value.exists()) {
                    if(value.getString("commentno") != null){
                        String comment = "" + value.getString("commentno");
                        int num = Integer.parseInt(comment) + 1;
                        String str_num = String.valueOf(num);
                        commentsMap.put("commentno", str_num);

                        updateField(commentsMap);
                    }else{
                        int num = 1;
                        String str_num = String.valueOf(num);
                        commentsMap.put("commentno", str_num);
                        updateField(commentsMap);

                    }
                    processComm = false;
                }
            }
        });

     }

    private void updateField(Map commentsMap) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("posts").document(post_id);
        documentReference.set(commentsMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("added");
            }
        });
    }


}

