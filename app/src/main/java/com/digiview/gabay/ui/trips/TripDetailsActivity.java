package com.digiview.gabay.ui.trips;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Item;
import com.digiview.gabay.services.FirebaseChildEventListenerCallback;
import com.digiview.gabay.services.ItemService;
import com.digiview.gabay.services.TripService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class TripDetailsActivity extends AppCompatActivity {

    private List<Item> tripItems = new ArrayList<>();
    private ItemsAdapter itemsAdapter;
    private String tripID;

    private ItemService itemService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trip_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        populateActivityWithPassedData();
        redirectToAddItem(tripID);
        modifyActionBar();

        Intent intent = getIntent();
        tripID = intent.getStringExtra("TRIP_ID");

        RecyclerView recyclerView = findViewById(R.id.TripDetails_RecyclerView);


        itemService = ItemService.getInstance();
        itemsAdapter = new ItemsAdapter(tripItems, new ItemsAdapter.OnItemRemoveListener() {

            @Override
            public void onItemRemove(int position) {
                // Remove item from Firebase and update the list
                Item item = tripItems.get(position);
                itemService.deleteItem(item);
            }
        }, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemsAdapter);
        
        addFirebaseChildListener();

    }

    private void addFirebaseChildListener() {
        itemService.addChildEventListenerForItemList(tripID, new FirebaseChildEventListenerCallback<DataSnapshot>() {
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Item item = snapshot.getValue(Item.class);
                tripItems.add(0, item);
                itemsAdapter.notifyItemInserted(0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = -1;
                for (int i = 0; i < tripItems.size(); i++) {
                    if (tripItems.get(i).trip_id.equals(snapshot.getKey())) {
                        index = i;
                        break;
                    }
                }
                if (index !=-1) {
                    tripItems.remove(index);
                    itemsAdapter.notifyItemRemoved(index);
                }
            }
        });
    }


    private void redirectToAddItem (String tripID) {
        ImageButton addItemButton = findViewById(R.id.TripDetails_Button_AddItem);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirectIntent = new Intent(TripDetailsActivity.this, AddItemActivity.class);
                redirectIntent.putExtra("TRIP_ID", tripID);
                startActivity(redirectIntent);
            }
        });
    }


    private void modifyActionBar() {
        // Set the status bar color programmatically
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        // Set the status bar text color to white
        WindowInsetsController insetsController = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController = window.getInsetsController();
        }
        if (insetsController != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insetsController.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
        }

        // Enable the action bar and set the close button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Ensure you have an ic_close drawable
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateActivityWithPassedData() {
        Intent intent = getIntent();
        // Check if extras are present
        if (intent != null && intent.getExtras() != null) {
            // Retrieve the trip name from the extras using getStringExtra()
            String tripName = intent.getStringExtra("TRIP_NAME");
            Double tripBudget = intent.getDoubleExtra("TRIP_BUDGET", 0.00);
            String formattedBudget = String.format("%,.2f", tripBudget);

            // Set Label
            setTitle(tripName);

            // Set the trip name to the TextView
            TextView textViewTripBudget = findViewById(R.id.TripDetails_Budget);
            textViewTripBudget.setText(String.format("₱ %s", formattedBudget));

        }
    }


}