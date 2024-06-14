package com.digiview.gabay.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.digiview.gabay.domain.entities.Item;
import com.digiview.gabay.domain.entities.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public final class TripService {
    // Declare all the needed variables
    private static TripService instance;
    private final FirebaseAuth fbAuth;
    private final FirebaseUser fbUser;
    private final FirebaseDatabase fbDB;
    private final DatabaseReference userDBRef;
    private final ItemService itemService;


    //Initialize all the needed variables
    //the constructor is private because this is a singleton
    private TripService() {
        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser();
        fbDB = FirebaseDatabase.getInstance();

        //uses userid as key for this node (i.e. table)
        assert fbUser != null;
        userDBRef = fbDB.getReference("trips/" + fbUser.getUid());
        //keeps all the contents in cache
        userDBRef.keepSynced(true);
        itemService = ItemService.getInstance();
    }

    public static synchronized TripService getInstance() {
        if (instance == null) {
            instance = new TripService();
        }
        return instance;
    }

    public void createTrip(Trip trip) {
        DatabaseReference newRef = userDBRef.push();
        trip.trip_id = newRef.getKey();
        newRef.setValue(trip);
    }

    public void editTrip(Trip trip) {
        DatabaseReference existingRef = userDBRef.child(trip.trip_id);
        existingRef.setValue(trip);
    }

    public void deleteTrip(Trip trip) {
        DatabaseReference existingRef = userDBRef.child(trip.trip_id);
        itemService.deleteAllItemWithTripID(trip.trip_id);
        existingRef.removeValue();
    }


    public void addChildEventListener(FirebaseChildEventListenerCallback<DataSnapshot> callback) {
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

    public void addValueEventListener(String key, FirebaseValueEventListenerCallback<DataSnapshot> callback) {
        DatabaseReference existingRef = userDBRef.child(key);
        existingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onDataChange(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCancelled(error);
            }
        });
    }
}
