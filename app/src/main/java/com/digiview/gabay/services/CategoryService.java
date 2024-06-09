package com.digiview.gabay.services;

import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.digiview.gabay.domain.entities.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryService {
    //Declare all the needed variables
    private static CategoryService instance;
    private final FirebaseAuth fbAuth;
    private final FirebaseUser fbUser;
    private final FirebaseDatabase fbDB;
    private final DatabaseReference userDBRef;

    //Initialize all the needed variables
    //the constructor is private because this is a singleton
    private CategoryService() {
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        fbDB = FirebaseDatabase.getInstance();

        //uses userid as key for this node (i.e. table)
        assert fbUser != null;
        userDBRef = fbDB.getReference("categories/" + fbUser.getUid());
        //keeps all the contents in cache
        userDBRef.keepSynced(true);

    }

    public static synchronized CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
        }
        return instance;
    }

    public void createCategory(Category category) {
        DatabaseReference newRef = userDBRef.push();
        category.category_id = newRef.getKey();
        newRef.setValue(category);
    }

    public void editCategory(Category category) {
        DatabaseReference existingRef = userDBRef.child(category.category_id);
        existingRef.setValue(category);
    }

    public void deleteCategory(Category category) {
        DatabaseReference existingRef = userDBRef.child(category.category_id);
        existingRef.removeValue();
    }

    public void addChildEventListener(FirebaseChildEventListenerCallback<DataSnapshot> callback){
        userDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callback.onChildAdded(snapshot, previousChildName);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callback.onChildChanged(snapshot, previousChildName);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                callback.onChildRemoved(snapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                callback.onChildMoved(snapshot, previousChildName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCancelled(error);
            }
        });
    }
}
