package com.example.navapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navapp.Utils.Comments;
import com.example.navapp.Utils.Users;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private Activity context;
    private List<Users> usersList;

    private List<Comments> commentsList;
    public FirebaseFirestore firestore;


    public CommentsAdapter(Activity context, List<Comments> commentsList, List<Users> usersList){
        this.context = context;
        this.commentsList = commentsList;
        this.usersList = usersList;
    }


    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_comment, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comments comments = commentsList.get(position);

        holder.setmcomment(comments.getComment());

        Users users = usersList.get(position);
        holder.setmUserName(users.getUsername());




    }

    @Override
    public int getItemCount() {
        if(commentsList != null){
            return commentsList.size();

        }
        return 0;
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView mcomment, mUserName;
        View mView;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setmcomment(String comment){
            mcomment = mView.findViewById(R.id.comment_tv);
            mcomment.setText(comment);
        }

        public void setmUserName(String username){
            mUserName = mView.findViewById(R.id.comment_user);
            mUserName.setText(username);
        }

    }
}
