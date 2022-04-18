package com.example.navapp.Utils;

import android.net.Uri;
import android.widget.ImageView;

public class Posts {
    private String datePost, description, title, username;
    private String likeBtn;

    public Posts() {
    }

    public Posts(String datePost, String description, String title, String username) {
        this.datePost = datePost;
        this.description = description;
        this.title = title;
        this.username = username;
        this.likeBtn = likeBtn;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
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

    public String getLikeBtn() {
        return likeBtn;
    }

    public void setLikeBtn(String likeBtn) {
        this.likeBtn = likeBtn;
    }

}
