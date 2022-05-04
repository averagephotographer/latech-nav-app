package com.example.navapp.Utils;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PostId {
    @Exclude
    public String PostId;



    public <T extends PostId>  T withId (@NonNull final String id){
        this.PostId = id;
        return (T) this;
    }

    public static <T extends PostId> T checkNotNull(
            @Nonnull T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static <T extends PostId> T checkNotNull(@Nonnull T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
