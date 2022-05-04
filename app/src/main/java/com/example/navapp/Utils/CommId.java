package com.example.navapp.Utils;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class CommId {
    @Exclude
    public String CommId;

    public <T extends CommId> T withId(@NonNull final String id){
        this.CommId = id;
        return (T) this;
    }
}
