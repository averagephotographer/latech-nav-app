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
import android.widget.TextView;
import android.widget.Toast;

import com.example.navapp.Utils.Posts;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {
    private BottomNavigationView btm_view;
    private FloatingActionButton fab;
    TextView showPost;
    SharedPreferences sharedPreferences;
    private RecyclerView recycler;
    //ArrayList<Posts> postsArrayList;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private mypostsAdapter adapter;
    private List<Posts> list;
    SharedPreferences sharedpref;
    private Query query;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recycler = findViewById(R.id.recyclerView);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(MyPostsActivity.this));

        list = new ArrayList<>();

        adapter = new mypostsAdapter(MyPostsActivity.this, list);
        recycler.setAdapter(adapter);

        btm_view = findViewById(R.id.bottomNavigationView);
        btm_view.setBackground(null);

        fab = findViewById(R.id.floatingActionButton);

        sharedpref = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String name = sharedpref.getString("username", "");



        if(firebaseAuth.getCurrentUser() != null){
            recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Boolean isbtm = !recycler.canScrollVertically(1);
                    if(isbtm)
                        Toast.makeText(MyPostsActivity.this, "You've reached the end", Toast.LENGTH_SHORT);
                }
            });
            query = firestore.collection("posts").whereEqualTo("username", name);
            listenerRegistration = query.addSnapshotListener(MyPostsActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            Posts post = doc.getDocument().toObject(Posts.class);
                            list.add(post);
                            adapter.notifyDataSetChanged();
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                    }
                    listenerRegistration.remove();
                }
            });

        }



    }
}