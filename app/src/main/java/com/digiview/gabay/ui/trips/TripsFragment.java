package com.digiview.gabay.ui.trips;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Trip;
import com.digiview.gabay.services.FirebaseChildEventListenerCallback;
import com.digiview.gabay.services.TripService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {

    // Pass the model
    // ArrayList<TripModel> tripModels = new ArrayList();
    private RecyclerView recyclerView;
    private TripsAdapter tripsAdapter;
    private List<Trip> trips;
    private TripService tripService;

    public TripsFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.Trips_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //setupModels
        trips = new ArrayList<>();
        tripsAdapter = new TripsAdapter(trips);

        //initialize service
        tripService = TripService.getInstance();
        addFirebaseChildListener();


        recyclerView.setAdapter(tripsAdapter);

        // fetchTripsFromFirebase();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = getActivity().findViewById(R.id.main_fab);
        fab.show(); // Make the FAB visible

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle FAB click for TripsFragment
                 Intent intent = new Intent(getActivity(), CreateTripActivity.class);
                 startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        FloatingActionButton fab = getActivity().findViewById(R.id.main_fab);
        fab.hide(); // Hide the FAB when the fragment is not visible
    }

//    private void fetchTripsFromFirebase() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("trips")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot document : task.getResult()) {
//                            TripModel trip = document.toObject(TripModel.class);
//                            tripModels.add(trip);
//                        }
//                        tripsAdapter.notifyDataSetChanged();
//                    } else {
//                        // Handle the error
//                    }
//                });
//    }

    //
    private void addFirebaseChildListener() {
        tripService.addChildEventListener(new FirebaseChildEventListenerCallback<DataSnapshot>() {
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Trip trip = snapshot.getValue(Trip.class);
                trips.add(0, trip);
                tripsAdapter.notifyItemInserted(0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Trip trip = snapshot.getValue(Trip.class);
                for (int i = 0; i < trips.size(); i++) {
                    if(trips.get(i).trip_id.equals(snapshot.getKey())) {
                        trips.set(i, trip);
                        tripsAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //not needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = -1;
                for (int i = 0; i < trips.size(); i++) {
                    if (trips.get(i).trip_id.equals(snapshot.getKey())) {
                        index = i;
                        break;
                    }
                }
                if (index !=-1) {
                    trips.remove(index);
                    tripsAdapter.notifyItemRemoved(index);
                }
            }
        });
    }
}