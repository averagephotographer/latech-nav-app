package com.example.navapp.Utils;

import android.net.Uri;
import android.widget.ImageView;

public class Posts extends PostId {
    private String datePost;
    private String description;
    private String title;
    private String username;
    private String uid;
    private String  commentno;
    private String imageURL;
    private String likes;

    public void setCommentno(String commentno) {
        this.commentno = commentno;
    }


    private String likeCount;

    public Posts() {
    }

    public String getUid() {
        return uid;
    }

    public Posts(String datePost, String description, String title, String username, String imageURL,  String uid, String commentno) {
        this.datePost = datePost;
        this.description = description;
        this.title = title;
        this.username = username;
        this.likes = likes;
        this.uid = uid;
        this.imageURL = imageURL;
        this.commentno = commentno;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getCommentno() {
        return commentno;
    }

    public String getLikes() {
        return likes;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

}
