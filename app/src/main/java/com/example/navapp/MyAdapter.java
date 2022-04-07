package com.example.navapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navapp.Utils.Posts;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Posts> postsArrayList;
    SharedPreferences sharedPreferences;


    public MyAdapter(Context context, ArrayList<Posts> postsArrayList) {
        this.context = context;
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_each_post,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username", "");

        Posts  myposts = postsArrayList.get(position);

        holder.postDesc.setText(myposts.getDescription());
        holder.postTitle.setText(myposts.getTitle());
        holder.postDate.setText(myposts.getDatePost());
        holder.username.setText(myposts.name);

    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView postDesc, postTitle, username, postDate, likeCount;
        ImageView likeButton, commentButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postDesc = itemView.findViewById(R.id.show_description);
            postTitle = itemView.findViewById(R.id.show_title);
            username = itemView.findViewById(R.id.username_tv);
            postDate = itemView.findViewById(R.id.date_tv);
            likeCount = itemView.findViewById(R.id.like_count_tv);
            likeButton = itemView.findViewById(R.id.like_btn);
            commentButton = itemView.findViewById(R.id.comments_post);
        }
    }
}
