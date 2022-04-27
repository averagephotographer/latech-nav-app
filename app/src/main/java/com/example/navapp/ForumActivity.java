package com.example.navapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.navapp.Utils.Posts;
import com.example.navapp.databinding.ActivityDrawerBaseBinding;
import com.example.navapp.databinding.ActivityForumBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.WatchChange;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;




public class ForumActivity extends DrawerBaseActivity  {
    ActivityForumBinding activityForumBinding;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    TextView showPost;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    ArrayList<Posts> postsArrayList;
    FirebaseFirestore firestore;
    MyAdapter myAdapter;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    String titlepost;
    String description;

    ImageView commentbtn;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForumBinding = ActivityForumBinding.inflate(getLayoutInflater());
        setContentView(activityForumBinding.getRoot());
        allocateActivityTitle("Forum");
        showPost = (TextView) findViewById(R.id.show_post);
        Intent intent = getIntent();
        String string = intent.getStringExtra("Value");
        showPost.setText(string);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        //progressDialog.show();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        commentbtn = findViewById(R.id.comments_post);

        firestore = FirebaseFirestore.getInstance();
        postsArrayList = new ArrayList<Posts>();
        myAdapter = new MyAdapter(ForumActivity.this, postsArrayList);

        recyclerView.setAdapter(myAdapter);
        EventChangeListener();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForumActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });

    }




    private void EventChangeListener() {

        sharedPreferences = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username", "");


        firestore.collection("posts").orderBy("datePost",Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore Error", error.getMessage());
                            return;

                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                String postId = dc.getDocument().getId();
                                postsArrayList.add(dc.getDocument().toObject(Posts.class).withId(postId));
                                myAdapter.notifyDataSetChanged();

                            }
                        }

                        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
                        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                myAdapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);

                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();


                    }
                });

    }


}

