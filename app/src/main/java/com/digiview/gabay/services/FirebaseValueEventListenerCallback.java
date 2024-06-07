package com.digiview.gabay.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface FirebaseValueEventListenerCallback<T> {
    public void onCancelled(@NonNull DatabaseError error);
    public void onDataChange(@NonNull T snapshot);
}
