package com.digiview.gabay.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

// an interface for creating a call that wraps the firebase event listener
public interface FirebaseValueEventListenerCallback<T> {
    public void onCancelled(@NonNull DatabaseError error);
    public void onDataChange(@NonNull T snapshot);
}
