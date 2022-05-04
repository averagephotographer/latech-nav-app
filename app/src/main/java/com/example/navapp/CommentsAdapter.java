package com.example.navapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Gravity;
import android.util.Log;
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
import com.example.navapp.Utils.Comments;
import com.example.navapp.Utils.Posts;
import com.example.navapp.Utils.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private Activity context;
    private List<Users> usersList;
    private List<Comments> commentsList;
    public FirebaseFirestore firestore;
    public FirebaseAuth fauth;
    public String cuid;
    public String myuid;
    String UID, post_id;
    DocumentReference ref;


    public CommentsAdapter(Activity context, List<Comments> commentsList, List<Users> usersList, String post_id, String UID){
        this.context = context;
        this.commentsList = commentsList;
        this.usersList = usersList;

        this.post_id = post_id;
        this.UID = UID;
        cuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseFirestore.getInstance().collection("posts").document(post_id);
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

        myuid = comments.getUid();
        String comid = comments.CommId;

        if(cuid.equals(myuid)){
            holder.delete_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.delete_icon.setVisibility(View.VISIBLE);
                    showMoreOptions(holder.delete_icon, post_id, comid, position);




                }
            });


        }else{
            holder.delete_icon.setVisibility(View.GONE);
        }


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


    private void showMoreOptions(ImageView moreBtn, String postid, String commid, int index){
        PopupMenu popupMenu = new PopupMenu(context, moreBtn, Gravity.END);

        //show delete option in only post(s) of currently signed-in user
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int id = menuItem.getItemId();
                if(id==0){
                    beginDelete(postid, commid, index);
                    //delete is clicked
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void beginDelete(String postid, String commid, int index) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting....");

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("posts").document(postid).collection("comments").document(commid)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    commentsList.remove(commentsList.get(index));
                    ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String a = documentSnapshot.getString("commentno");
                            int b = (Integer.parseInt(a))-1;
                            ref.update("commentno", String.valueOf(b));

                        }
                    });

                    notifyDataSetChanged();
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();


                    pd.dismiss();
                }else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

        });


    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        TextView mcomment, mUserName;
        ImageView delete_icon;
        CircleImageView circleImageView;
        View mView;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            delete_icon = mView.findViewById(R.id.delete);
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
