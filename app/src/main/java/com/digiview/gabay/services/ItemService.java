package com.digiview.gabay.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.digiview.gabay.domain.entities.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ItemService {
    // Declare all the needed variables
    private static ItemService instance;
    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser;
    private FirebaseDatabase fbDB;
    private DatabaseReference userDBRef;
    private DatabaseReference userDBRef2;

    //Initialize all the needed variables
    //the constructor is private because this is a singleton
    public ItemService() {
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        fbDB = FirebaseDatabase.getInstance();

        //uses userid as key for this node (i.e. table)
        assert fbUser != null;
        userDBRef = fbDB.getReference("items/" + fbUser.getUid());
        userDBRef2 = fbDB.getReference("items-linear/" + fbUser.getUid());
        userDBRef.keepSynced(true);
        userDBRef2.keepSynced(true);
    }

    // gets a single instance of ItemService
    public static synchronized ItemService getInstance() {
        if(instance==null) {
            instance = new ItemService();
        }
        return instance;
    }

    // create an item record in the database
    public void createItem(Item item) {
        DatabaseReference newRefLinear = userDBRef2.push();
        String newKey = newRefLinear.getKey();
        assert newKey != null;
        item.item_id = newKey;
        DatabaseReference newRefItem = userDBRef.child(item.trip_id).child(newKey);
        newRefLinear.setValue(item);
        newRefItem.setValue(item);


    }

    // edit an item record in the database
    public void editItem(Item item) {
        DatabaseReference existingRefLinear = userDBRef2.child(item.item_id);
        DatabaseReference existingRefItem = userDBRef.child(item.trip_id).child(item.item_id);
        existingRefLinear.setValue(item);
        existingRefItem.setValue(item);
    }

    // delete an item record in the database
    public void deleteItem(Item item) {
        DatabaseReference existingRefLinear = userDBRef2.child(item.item_id);
        DatabaseReference existingRefItem = userDBRef.child(item.trip_id).child(item.item_id);
        existingRefLinear.removeValue();
        existingRefItem.removeValue();
    }

    // delete all related item to a trip
    public void deleteAllItemWithTripID(String tripID) {
        DatabaseReference existingRefItem = userDBRef.child(tripID);
        Query query = userDBRef2.orderByChild("trip_id").equalTo(tripID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String currentChildKey = child.getKey();
                    if (currentChildKey != null) {
                        DatabaseReference currentChild = userDBRef2.child(currentChildKey);
                        currentChild.removeValue();
                    }
                }
                existingRefItem.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        existingRefItem.removeValue();
    }

    // listens to the changes in a list of record in the database related to a trip
    public void addChildEventListenerForItemList(String tripID, FirebaseChildEventListenerCallback<DataSnapshot> callback) {
        DatabaseReference existingRefItem = userDBRef.child(tripID);
        existingRefItem.addChildEventListener(new ChildEventListener() {
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

    // listens to the changes in a list of record in the database
    public void addChildEventListenerForRecentAddedItem(FirebaseChildEventListenerCallback<DataSnapshot> callback) {
        userDBRef2.addChildEventListener(new ChildEventListener() {
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
