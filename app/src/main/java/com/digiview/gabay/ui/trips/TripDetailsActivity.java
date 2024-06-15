package com.digiview.gabay.ui.trips;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TripDetailsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private List<Item> tripItems = new ArrayList<>();
    private ItemsAdapter itemsAdapter;
    private String tripID;
    private Double tripBudget;
    private ItemService itemService;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trip_details);

        tts = new TextToSpeech(this, this);
        tts.setLanguage(Locale.US);

        // Initialize RecyclerView and adapter
        RecyclerView recyclerView = findViewById(R.id.TripDetails_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsAdapter = new ItemsAdapter(tripItems, new ItemsAdapter.OnItemRemoveListener() {
            @Override
            public void onItemRemove(int position) {
                showDeleteConfirmationDialog(position);
            }
        }, this);
        recyclerView.setAdapter(itemsAdapter);

        // Retrieve tripID and tripBudget from intent
        Intent intent = getIntent();
        tripID = intent.getStringExtra("TRIP_ID");
        tripBudget = intent.getDoubleExtra("TRIP_BUDGET", 0.00);

        // Populate UI with passed data
        populateActivityWithPassedData();
        redirectToAddItem(tripID);
        modifyActionBar();

        // Initialize ItemService for Firebase operations
        itemService = ItemService.getInstance();

        // Calculate total expense and update TextView
        updateTotalExpense();

        // Setup Firebase listener to update tripItems
        addFirebaseChildListener();
    }

    private void updateTotalExpense() {
        double totalExpense = calculateTotalExpense();
        TextView textViewExpense = findViewById(R.id.TripDetails_Expense);
        String formattedExpense = String.format("₱ %.2f", totalExpense);
        textViewExpense.setText(formattedExpense);

        // Calculate remaining balance and update TextView
        double remainingBalance = tripBudget - totalExpense;
        TextView textViewRemainingBalance = findViewById(R.id.TripDetails_RemainingBalance);
        String formattedRemainingBalance = String.format("₱ %.2f", remainingBalance);
        textViewRemainingBalance.setText(formattedRemainingBalance);
    }

    private double calculateTotalExpense() {
        double total = 0.0;
        for (Item item : tripItems) {
            total += item.item_cost;
        }
        return total;
    }

    private void addFirebaseChildListener() {
        itemService.addChildEventListenerForItemList(tripID, new FirebaseChildEventListenerCallback<DataSnapshot>() {
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Item item = snapshot.getValue(Item.class);
                tripItems.add(0, item); // Add at the beginning for newest items
                itemsAdapter.notifyItemInserted(0);
                updateTotalExpense(); // Update total expense after adding item
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle item update if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle item moved if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle item removal
                String itemKey = snapshot.getKey();
                for (int i = 0; i < tripItems.size(); i++) {
                    if (tripItems.get(i).item_id.equals(itemKey)) {
                        tripItems.remove(i);
                        itemsAdapter.notifyItemRemoved(i);
                        updateTotalExpense(); // Update total expense after removing item
                        break;
                    }
                }
            }
        });
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);
        builder.setTitle("Are you sure you want to delete this item?");
        builder.setMessage(getResources().getString(R.string.delete_trip_details));
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Item item = tripItems.get(position);
                itemService.deleteItem(item);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                tts.speak(getResources().getString(R.string.delete_trip_details), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        dialog.show();
    }

    private void redirectToAddItem(String tripID) {
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
        // Modify action bar as needed
        // Ensure you handle back button correctly
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Use your own icon
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateActivityWithPassedData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String tripName = intent.getStringExtra("TRIP_NAME");
            String formattedBudget = String.format("₱ %.2f", tripBudget);
            setTitle(tripName);
            TextView textViewTripBudget = findViewById(R.id.TripDetails_Budget);
            textViewTripBudget.setText(formattedBudget);
        }
    }

    @Override
    public void onInit(int status) {

    }
}