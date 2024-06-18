package com.digiview.gabay.ui.trips;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.digiview.gabay.domain.entities.Trip;
import com.digiview.gabay.services.TripService;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.digiview.gabay.R;

public class CreateTripActivity extends AppCompatActivity implements  View.OnClickListener, TextToSpeech.OnInitListener {
    //CONSTRUCTORS

    //CLASS VARIABLES
    private EditText inputName;
    private EditText inputBudget;
    private EditText inputStartDate;
    private Button saveButton;
    private TripService tripService;
    private TextToSpeech tts;

    //ANDROID LIFECYCLE OVERRIDES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_trip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tts = new TextToSpeech(this, this);
        tts.setLanguage(Locale.US);

        modifyActionBar();

        tripService = TripService.getInstance();

        // Add DatePicker upon clicking on date input
        inputName = findViewById(R.id.CreateTrip_InputTripName);
        inputBudget = findViewById(R.id.CreateTrip_InputBudget);
        inputStartDate = findViewById(R.id.CreateTrip_InputStartDate);
        saveButton = findViewById(R.id.button_CreateTrip);

        saveButton.setOnClickListener(this);

        addDatePicker(inputStartDate);



    }

    //OTHER OVERRIDES
    @Override
    public void onClick(View v) {
        int currentID = v.getId();
        if (currentID == R.id.button_CreateTrip) {
            onTripSave();
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the close button click
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogTheme);
            builder.setTitle("Unsaved Changes");
            builder.setMessage(getResources().getString(R.string.unsaved_changes_dialog));
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Perform logout
                    finish();
                }
            });
            // Add the negative button (No)
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Dismiss the dialog if "No" is clicked
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    tts.speak(getResources().getString(R.string.unsaved_changes_dialog), TextToSpeech.QUEUE_FLUSH, null);
                }
            });
            dialog.show();



            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //CUSTOM FUNCTIONS/METHODS
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
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close); // Ensure you have an ic_close drawable
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void addDatePicker(EditText inputField){
        inputField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setTheme(R.style.CustomMaterialDatePicker)
                        .build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        // Update the date format as needed
                        String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date(selection));
                        inputField.setText(MessageFormat.format("{0}", date));
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "tag");
            }
        });
    }

    public void onTripSave() {
        Trip trip = new Trip();
        trip.trip_name = inputName.getText().toString();
        trip.trip_budget = Double.valueOf(inputBudget.getText().toString());
        trip.trip_date = inputStartDate.getText().toString();
        tripService.createTrip(trip);
    }

    @Override
    public void onInit(int status) {

    }
}
