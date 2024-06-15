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
import android.widget.Toast;

import com.digiview.gabay.MainActivity;
import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Category;
import com.digiview.gabay.domain.entities.Trip;
import com.digiview.gabay.services.FirebaseChildEventListenerCallback;
import com.digiview.gabay.services.TripService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment implements TripInterface{

    // Pass the model
    // ArrayList<TripModel> tripModels = new ArrayList();
    private RecyclerView recyclerView;
    private TripsAdapter tripsAdapter;
    private List<Trip> trips;
    private TripService tripsService;

    public TripsFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerView = view.findViewById(R.id.Trips_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //setupModels

        trips = new ArrayList<>();
        tripsAdapter = new TripsAdapter(getContext(), trips, this);

        //initialize service
        tripsService = TripService.getInstance();
        addFirebaseChildListener();

        recyclerView.setAdapter(tripsAdapter);


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

    //
    private void addFirebaseChildListener() {
        tripsService.addChildEventListener(new FirebaseChildEventListenerCallback<DataSnapshot>() {
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
                Trip updatedTrip = snapshot.getValue(Trip.class);
                if (updatedTrip == null) return;

                int index = -1;
                for (int i = 0; i < trips.size(); i++) {
                    if (trips.get(i).trip_id.equals(updatedTrip.trip_id)) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    trips.set(index, updatedTrip);
                    tripsAdapter.notifyItemChanged(index);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(requireContext(), TripDetailsActivity.class);

        intent.putExtra("TRIP_ID", trips.get(position).trip_id);
        intent.putExtra("TRIP_NAME", trips.get(position).trip_name);
        intent.putExtra("TRIP_DATE", trips.get(position).trip_date);
        intent.putExtra("TRIP_BUDGET", trips.get(position).trip_budget);

        startActivity(intent);

    }
}