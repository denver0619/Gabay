package com.digiview.gabay.ui.trips;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiview.gabay.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TripsFragment extends Fragment {

    // Pass the model
    // ArrayList<TripModel> tripModels = new ArrayList();
    private RecyclerView recyclerView;
    private TripsAdapter tripsAdapter;
    private List<TripModel> tripModels;

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
        tripModels = new ArrayList<>();
        tripsAdapter = new TripsAdapter(tripModels);
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
}