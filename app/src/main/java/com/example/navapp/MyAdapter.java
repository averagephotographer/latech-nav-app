package com.example.navapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navapp.Utils.Posts;

import java.text.CollationElementIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    static Context context;
    ArrayList<Posts> postsArrayList;
    SharedPreferences sharedPreferences;
    ImageView comment_icon;



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

        Posts posts = postsArrayList.get(position);

        holder.postDesc.setText(posts.getDescription());
        holder.postTitle.setText(posts.getTitle());
        String timeAgo = calculateTimeAgo(posts.getDatePost());
        holder.postDate.setText(timeAgo);
        holder.username.setText(posts.getUsername());


    }

    private String calculateTimeAgo(String datePost) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        try {
            long time = sdf.parse(datePost).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView postDesc, postTitle, username, likeCount, postDate;
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
