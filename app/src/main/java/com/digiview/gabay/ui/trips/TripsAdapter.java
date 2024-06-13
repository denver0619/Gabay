package com.digiview.gabay.ui.trips;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Trip;

import java.lang.reflect.Field;
import java.util.List;

class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewHolder> {

    private final TripInterface tripInterface;
    private final List<Trip> tripList;
    private final Context context;

    public TripsAdapter(Context context, List<Trip> tripList, TripInterface tripInterface) {
        this.tripList = tripList;
        this.context = context;
        this.tripInterface = tripInterface;
    }

    @NonNull
    @Override
    public TripsAdapter.TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout (giving a look to our rows)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_recycler_view_row, parent, false);
        return new TripsAdapter.TripViewHolder(view, tripInterface, context);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsAdapter.TripViewHolder holder, int position) {
        // Assigning values to the views created in recycler row layout based on the position of recycler view
        Trip trip = tripList.get(position);
        holder.tripName.setText(trip.trip_name);
        holder.tripDate.setText(trip.trip_date);
    }

    @Override
    public int getItemCount() {
        // count how many items you have which helps in onBind
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {

        // grabbing views from recycler row layout file
        TextView tripName;
        TextView tripDate;
        ImageView kebabMenu;
        private final Context context;

        public TripViewHolder(@NonNull View itemView, TripInterface tripInterface, Context context) {
            super(itemView);

            this.context = context;
            tripName = itemView.findViewById(R.id.Trips_TripTitle);
            tripDate = itemView.findViewById(R.id.Trips_TripDate);
            kebabMenu = itemView.findViewById(R.id.Trips_Kebab);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tripInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            tripInterface.onItemClick(position);
                        }
                    }
                }
            });

            kebabMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view, getAdapterPosition());
                }
            });
        }

        private void showPopupMenu(View view, int position) {
            PopupMenu popup = new PopupMenu(context, view, Gravity.END, 0, R.style.PopupMenuStyle);
            // Inflate the menu
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.trips_option, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_edit:
                            // Handle edit click
                            Toast.makeText(context, "Edit clicked for position " + position, Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.action_delete:
                            // Handle delete click
                            Toast.makeText(context, "Delete clicked for position " + position, Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
        }
    }
}
