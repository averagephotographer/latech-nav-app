package com.example.navapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navapp.Utils.Posts;

import java.util.List;

public class mypostsAdapter extends RecyclerView.Adapter<mypostsAdapter.PostViewHolder> {


    private List<Posts> mList;
    private Activity context;

    public mypostsAdapter(Activity context, List<Posts> mList){
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_mypost, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Posts post = mList.get(position);
        holder.setCaption(post.getDescription());


        //long milliseconds = post.getDatePost().getTime();


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView like, comment, delete;
        TextView username, date, descrip, title, postlikes;
        View mView;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setPostusername(String postusername){
            username = mView.findViewById(R.id.username_tv);
            username.setText(postusername);

        }

        public void setDate(int postdate){
            date = mView.findViewById(R.id.date_tv);
            date.setText(postdate);
        }

        public void setCaption(String caption){
            descrip = mView.findViewById(R.id.show_description);
            descrip.setText(caption);
        }

        public void setTitle(String posttitle){
            title = mView.findViewById(R.id.show_title);
            title.setText(posttitle);
        }
    }

}
