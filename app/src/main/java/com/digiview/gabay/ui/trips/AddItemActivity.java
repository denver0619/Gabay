package com.digiview.gabay.ui.trips;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Category;
import com.digiview.gabay.ui.categories.CustomCategorySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private CustomCategorySpinnerAdapter adapter;
    private List<Category> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categorySpinner = findViewById(R.id.AddItem_Category);

        // Initialize categories
        categories = new ArrayList<>();

        // Insert way to access category from db

            // categories.add(new Category("Food", R.drawable.food_icon));
            // categories.add(new Category("Travel", R.drawable.travel_icon));
            // categories.add(new Category("Shopping", R.drawable.shopping_icon));


        // Set up adapter
        // adapter = new CustomCategorySpinnerAdapter(this, categories);
        // categorySpinner.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the close button click
            finish();
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
}