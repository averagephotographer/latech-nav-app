package com.example.navapp.Utils;

import android.content.Context;

public class Posts {
    public int name;
    private String datePost, description, title;

    public Posts() {
    }

    public Posts(String datePost, String description, String title) {
        this.datePost = datePost;
        this.description = description;
        this.title = title;
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

}
