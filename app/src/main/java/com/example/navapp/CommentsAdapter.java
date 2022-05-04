package com.example.navapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navapp.Utils.Comments;
import com.example.navapp.Utils.Posts;
import com.example.navapp.Utils.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private Activity context;
    private List<Users> usersList;
    private List<Comments> commentsList;
    public FirebaseFirestore firestore;


    public CommentsAdapter(Activity context, List<Comments> commentsList, List<Users> usersList){
        this.context = context;
        this.commentsList = commentsList;
        this.usersList = usersList;
        firestore = FirebaseFirestore.getInstance();

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
        holder.setCProfilePic(users.getImage());

        String userId = users.getUsername();

        firestore.collection("user_profile").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String image = task.getResult().getString("profilePicURL");
                    // check if user has a profile picture
                    // if they dont, then just put default picture
                    if (image == null) {
                        holder.circleImageView.setImageDrawable((context.getDrawable(R.drawable.elcipse)));
                    }
                    else {
                        holder.setCProfilePic(image);
                    }
                }
                else {
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView mcomment, mUserName;
        CircleImageView circleImageView;
        View mView;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCProfilePic(String urlProfile){
            circleImageView = mView.findViewById(R.id.comment_profile_pic);
            Glide.with(context).load(urlProfile).into(circleImageView);
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
