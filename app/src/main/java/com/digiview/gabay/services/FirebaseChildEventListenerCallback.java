package com.digiview.gabay.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

// an interface for creating a call that wraps the firebase event listener
public interface FirebaseChildEventListenerCallback<T> {
    public void onCancelled(@NonNull DatabaseError error);
    public void onChildAdded(@NonNull T snapshot, @Nullable String previousChildName);
    public void onChildChanged(@NonNull T snapshot, @Nullable String previousChildName);
    public void onChildMoved(@NonNull T snapshot, @Nullable String previousChildName);
    public void onChildRemoved(@NonNull T snapshot);
}
