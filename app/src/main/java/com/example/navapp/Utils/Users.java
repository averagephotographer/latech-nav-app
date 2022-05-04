package com.example.navapp.Utils;

public class Users {

    private String profilePicURL;
    private String username;

    public Users() {
    }

    public Users(String username, String profilePicURL) {
        this.username = username;
        this.profilePicURL = profilePicURL;
    }

    public String getImage() {
        return profilePicURL;
    }

    public void setImage() { this.profilePicURL = profilePicURL; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
