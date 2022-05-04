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
import com.example.navapp.Utils.Users;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.StorageReference;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    static Context context;
    private List<Users> usersList;
    ArrayList<Posts> postsArrayList;
    SharedPreferences sharedPreferences;

    FirebaseFirestore firestore;

    String myUid;
    String uid;
    String caption;
    String description;
    String user;
    String date;
    StorageReference storageReference;
    FirebaseAuth auth;
    //public String  postId = posts.PostId;

    boolean processlike = false;



    public MyAdapter(Context context, ArrayList<Posts> postsArrayList, ArrayList<Users> usersList) {
        this.context = context;
        this.postsArrayList = postsArrayList;
        this.usersList = usersList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_each_post,parent,false);
        auth = FirebaseAuth.getInstance();
        //FirebaseFirestore firestore = FirebaseFirestore.getInstance();
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
        String num = postsArrayList.get(position).getCommentno();
        //String likes = posts.getLikes();

        holder.postDesc.setText(posts.getDescription());
        holder.postTitle.setText(posts.getTitle());
        String timeAgo = calculateTimeAgo(posts.getDatePost());
        holder.postDate.setText(timeAgo);
        holder.username.setText(posts.getUsername());
        //holder.likeCount.setText(likes);
        
        holder.setPostPic(posts.getImageURL());
        String postId = posts.PostId;
        String currentuser = auth.getCurrentUser().getUid();

        //setLikes(holder, postId);

        //String num = posts.getCommentsno();
        System.out.println("num" + num);
        //String num1 = String.valueOf(num);
        if(num != null){
            holder.comment_no.setText(num);

        }
        else{
            System.out.println("null");
        }

        //holder.likeButton.setText(posts.getLikeBtn());


        System.out.println("post" + " " + postId);


        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context , CommentsActivity.class);
                commentIntent.putExtra("postid", postId);
                context.startActivity(commentIntent);
            }
        });
        String userId = posts.getUsername();

        firestore.collection("user_profile").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String image = task.getResult().getString("profilePicURL");
                    // check if user has a profile picture
                    // if they dont, then just put default picture
                    if (image == null) {
                        holder.profPic.setImageDrawable((context.getDrawable(R.drawable.elcipse)));
                    }
                    else {
                        holder.setProfilePic(image);
                    }
                }
                else {
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("posts", postId);
        firestore.collection("posts").document(postId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String postImage = task.getResult().getString("imageURL");
                    // check if user has a profile picture
                    // if they dont, then just put default picture
                    if (postImage == null) {
                        holder.postPic.setImageDrawable((context.getDrawable(R.drawable.ic_baseline_image_24)));
                    }
                    else {
                        holder.setPostPic(postImage);
                    }
                }
                else {
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });



        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("posts/" + postId + "/Likes").document(myUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){
                            Map<String, Object> likesmap = new HashMap<>();
                            likesmap.put("timestamp", FieldValue.serverTimestamp());
                            firestore.collection("posts/" + postId + "/Likes").document(myUid).set(likesmap);
                        }else{
                            firestore.collection("posts/" + postId + "/Likes").document(myUid).delete();
                        }

                    }
                });

            }
        });



        firestore.collection("posts/" + postId + "/Likes").document(currentuser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    if(value != null && value.exists()){
                        holder.likeButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_favorite_24));
                    }else{
                        holder.likeButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_favorite_border_24));
                    }
                }else{
                    System.out.println(error);
                }
            }
        });

        firestore.collection("posts/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    if(!value.isEmpty()){
                        String count =  "" + value.size();
                        holder.setPostLike(count);

                    }else{
                        holder.setPostLike("0");

                    }
                }
            }
        });

    }



    private String calculateTimeAgo(String datePost) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        try {
            long time = sdf.parse(datePost).getTime();
            long now = System.currentTimeMillis();
            Log.d("time", String.valueOf(time));
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            Log.d("ago", String.valueOf(ago));
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
        ImageView likeButton, commentButton, delete_btn, postPic;
        CircleImageView profPic;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postDesc = itemView.findViewById(R.id.show_description);
            postTitle = itemView.findViewById(R.id.show_title);
            username = itemView.findViewById(R.id.username_tv);
            postDate = itemView.findViewById(R.id.date_tv);
            likeButton = itemView.findViewById(R.id.like_btn);
            commentButton = itemView.findViewById(R.id.comments_post);
            delete_btn = itemView.findViewById(R.id.more_btn);
            comment_no = itemView.findViewById(R.id.comment_count);
            //likeCount = itemView.findViewById(R.id.like_count_tv);
            profPic = itemView.findViewById(R.id.profile_pic);
            likeCount = itemView.findViewById(R.id.like_count_tv);
        }

        public void setPostLike(String count) {
            likeCount = itemView.findViewById(R.id.like_count_tv);
            likeCount.setText(count + " Likes");
        }

        public void setProfilePic(String urlProfile){
            profPic = itemView.findViewById(R.id.profile_pic);
            Glide.with(context).load(urlProfile).into(profPic);
        }

        public void setCommentno(int count){
            comment_no = itemView.findViewById(R.id.comment_count);
            comment_no.setText(count);
        }

        public void setPostPic(String urlPost){
            postPic = itemView.findViewById(R.id.postImage);
            Glide.with(context).load(urlPost).into(postPic);
        }
    }


}
