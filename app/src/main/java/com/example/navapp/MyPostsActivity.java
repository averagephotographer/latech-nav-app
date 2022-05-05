package com.example.navapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navapp.Utils.Mypost;
import com.example.navapp.Utils.Posts;
import com.example.navapp.databinding.ActivityForumBinding;
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

public class MyPostsActivity extends DrawerBaseActivity {
    private FloatingActionButton fab;
    TextView showPost;
    SharedPreferences sharedPreferences;
    private RecyclerView recycler;
    //ArrayList<Posts> postsArrayList;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private mypostsAdapter adapter;
    private ArrayList<Posts> list;
    SharedPreferences sharedpref;
    private Query query;
    private BottomNavigationView bottomNavigationView;
    private ActivityForumBinding activityForumBinding;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForumBinding = ActivityForumBinding.inflate(getLayoutInflater());
        setContentView(activityForumBinding.getRoot());
        allocateActivityTitle("Forum");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recycler = findViewById(R.id.recyclerView);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(MyPostsActivity.this));

        list = new ArrayList<Posts>();

        adapter = new mypostsAdapter(MyPostsActivity.this, list);
        recycler.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.Account);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Account:
                    return true;
                case R.id.Home:
                    startActivity(new Intent(getApplicationContext(), ForumActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
        fab = findViewById(R.id.floatingActionButton);

        sharedpref = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String name = sharedpref.getString("username", "");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPostsActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });


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

            query = firestore.collection("posts").whereEqualTo("username", name).orderBy("datePost",Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener(MyPostsActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            String pid = doc.getDocument().getId();
                            Posts post = doc.getDocument().toObject(Posts.class).withId(pid);
                            //System.out.println("mypost" + post);
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