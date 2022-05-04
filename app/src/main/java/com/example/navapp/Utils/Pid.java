package com.example.navapp.Utils;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class Pid {
    @Exclude
    public String Pid;

    public <T extends Pid> T withId(@NonNull final String id){
        this.Pid = id;
        return (T) this;
    }
}
