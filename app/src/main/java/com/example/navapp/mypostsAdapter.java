package com.example.navapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.navapp.Utils.Mypost;
import com.example.navapp.Utils.PostId;
import com.example.navapp.Utils.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class mypostsAdapter extends RecyclerView.Adapter<mypostsAdapter.PostViewHolder> {
    String myUid;
    String uid;


    private ArrayList<Posts> mList;
    private Activity context;
    private FirebaseFirestore fstore;

    public mypostsAdapter(Activity context, ArrayList<Posts> mList){
        this.mList = mList;
        this.context = context;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_mypost, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder,  int position) {
        Posts post = mList.get(position);
        uid = post.getUid();
        holder.setCaption(post.getDescription());
        holder.setTitle(post.getTitle());
        holder.setPostusername(post.getUsername());
        String timeAgo = calculateTimeAgo(post.getDatePost());
        holder.setDate(timeAgo);
        String postId = post.PostId;
        System.out.println(postId);
        //int pos = holder.getBindingAdapterPosition();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptions(holder.delete, postId, position);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(context , CommentsActivity.class);
                commentIntent.putExtra("postid", postId);
                context.startActivity(commentIntent);
            }
        });

        holder.commcount.setText(post.getCommentno());

        fstore.collection("posts/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    if(!value.isEmpty()){
                        String count =  "" + value.size();
                        holder.postlikes.setText(count);

                    }else{
                        holder.postlikes.setText("0");

                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void showMoreOptions(ImageView moreBtn, String postid, int index) {
        PopupMenu popupMenu = new PopupMenu(context, moreBtn, Gravity.END);

        //show delete option in only post(s) of currently signed-in user
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
        System.out.println("post" + postid);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int id = menuItem.getItemId();
                if (id == 0) {
                    beginDelete(index, postid);
                    //delete is clicked
                }
                return false;
            }
        });
        popupMenu.show();



    }

    private void beginDelete(int pos, String postID){
        ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting....");

        FirebaseFirestore db = FirebaseFirestore.getInstance();





        db.collection("posts").document(postID).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot snapshot : task.getResult()){
                    db.collection("posts/" + postID + "/comments").document(snapshot.getId()).delete();
                }
            }
        });


        db.collection("posts").document(postID)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mList.remove(mList.get(pos));
                    notifyDataSetChanged();
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();


                    pd.dismiss();
                }else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

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


    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView like, comment, delete;
        TextView username, date, descrip, title, postlikes, commcount;
        View mView;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            delete = mView.findViewById(R.id.more_btn);
            comment = mView.findViewById(R.id.comments_post);
            postlikes = mView.findViewById(R.id.like_count_tv);
            commcount = mView.findViewById(R.id.com_count);
        }

        public void setPostusername(String postusername){
            username = mView.findViewById(R.id.username_tv);
            username.setText(postusername);

        }

        public void setDate(String postdate){
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
