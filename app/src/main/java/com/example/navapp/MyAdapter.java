package com.example.navapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navapp.Utils.Posts;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.snov.timeagolibrary.PrettyTimeAgo;

import java.text.CollationElementIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;




public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    static Context context;
    ArrayList<Posts> postsArrayList;
    SharedPreferences sharedPreferences;
    FirebaseFirestore firestore;
    String myUid;
    String uid;
    String caption;
    String description;
    String user;
    String date;
    //public String  postId = posts.PostId;



    public MyAdapter(Context context, ArrayList<Posts> postsArrayList) {
        this.context = context;
        this.postsArrayList = postsArrayList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        uid = posts.getUid();
        caption = posts.getTitle();
        description = posts.getDescription();
        user = posts.getUsername();
        date = posts.getDatePost();

        holder.postDesc.setText(posts.getDescription());
        holder.postTitle.setText(posts.getTitle());
        String timeAgo = calculateTimeAgo(posts.getDatePost());
        holder.postDate.setText(timeAgo);
        holder.username.setText(posts.getUsername());
        //holder.likeButton.setText(posts.getLikeBtn());

        String postId = posts.PostId;


        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context , CommentsActivity.class);
                commentIntent.putExtra("postid", postId);
                context.startActivity(commentIntent);
            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions(holder.delete_btn, uid, myUid, caption, user, date, description, postId,position);
            }
        });





        //like button
        //String postId = posts.PostId;


        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("posts/" + postId + "/likes").document(name);
            }
        });

        //comment count
        /*
        firestore.collection("posts/" + postId + "/comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    int count = value.size();
                    System.out.println(count);
                    holder.setCommentno(count);

                }else{
                    holder.setCommentno(0);

                }
            }

        });*/





    }

    private String calculateTimeAgo(String datePost) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        try {
            long time = sdf.parse(datePost).getTime();
            long now = System.currentTimeMillis();
            Log.e(String.valueOf(now), "");
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }



    private void showMoreOptions(ImageView moreBtn, String uid, String myUid, String postid, String cap, String user_name, String time, String descrip, int index){
        PopupMenu popupMenu = new PopupMenu(context, moreBtn, Gravity.END);

        //show delete option in only post(s) of currently signed-in user
        if(uid.equals(myUid)){
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");

        }


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int id = menuItem.getItemId();
                if(id==0){
                    beginDelete(postid, index);
                    //delete is clicked
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void beginDelete(String postId, int pos){
        ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting....");

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("posts").document(postsArrayList.get(pos).PostId)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    postsArrayList.remove(postsArrayList.get(pos));
                    notifyDataSetChanged();
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();


                    pd.dismiss();
                }else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

        });



    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView postDesc, postTitle, username, likeCount, postDate,comment_no;
        ImageView likeButton, commentButton, delete_btn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postDesc = itemView.findViewById(R.id.show_description);
            postTitle = itemView.findViewById(R.id.show_title);
            username = itemView.findViewById(R.id.username_tv);
            postDate = itemView.findViewById(R.id.date_tv);
            likeButton = itemView.findViewById(R.id.like_btn);
            commentButton = itemView.findViewById(R.id.comments_post);
            delete_btn = itemView.findViewById(R.id.more_btn);
        }

        public void setPostLike(int count) {
            likeCount = itemView.findViewById(R.id.like_count_tv);
            likeCount.setText(count);
        }

        public void setCommentno(int count){
            comment_no = itemView.findViewById(R.id.comment_count);
            comment_no.setText(count + "comments");
        }
    }


}
