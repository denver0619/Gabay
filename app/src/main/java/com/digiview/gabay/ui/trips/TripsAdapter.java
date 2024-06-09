package com.digiview.gabay.ui.trips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.gabay.R;

import java.util.ArrayList;

class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewHolder> {

    List<TripModel> tripList;
    public TripsAdapter(List<TripModel> tripList){
        this.tripList = tripList;
    }
    @NonNull
    @Override
    public TripsAdapter.TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout (giving a look to our rows)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_recycler_view_row, parent, false);
        return new TripsAdapter.TripViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TripsAdapter.TripViewHolder holder, int position) {
        // Assigning values to the views created in recycler row layout based on the position of recycler view
        TripModel trip = tripList.get(position);
        holder.tripName.setText(trip.getName());
        holder.tripDate.setText(trip.getDate());

    }

    @Override
    public int getItemCount() {
        // count how many items you have which helps in onBind
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {

        // some sort of onCreateMethod
        // grabbing views from recycler row layout file
        TextView tripName;
        TextView tripDate;
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);

            tripName = itemView.findViewById(R.id.Trips_TripTitle);
            tripDate = itemView.findViewById(R.id.Trips_TripDate);

        }
    }
}
