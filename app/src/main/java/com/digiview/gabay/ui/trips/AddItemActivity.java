package com.digiview.gabay.ui.trips;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Category;
import com.digiview.gabay.services.CategoryService;
import com.digiview.gabay.ui.categories.CustomCategorySpinnerAdapter;
import com.digiview.gabay.services.FirebaseChildEventListenerCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private CustomCategorySpinnerAdapter adapter;

    private List<Category> categories;
    private CategoryService categoryService;
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
        // Initialize CategoryService
        categoryService = CategoryService.getInstance();
        addFirebaseChildListener();

        // Set up adapter
        adapter = new CustomCategorySpinnerAdapter(this, categories);
        categorySpinner.setAdapter(adapter);
        modifyActionBar();

        // Assuming you have a button for adding an item
        Button addButton = findViewById(R.id.AddItem_Button_AddItem); // Ensure you have this button in your layout
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category selectedCategory = (Category) categorySpinner.getSelectedItem();
                if (selectedCategory != null) {
                    TextView tv = findViewById(R.id.selectedIconTextView);
                    ImageView iv = findViewById(R.id.selectedIconImageView);

                    tv.setText(selectedCategory.category_icon);
                    iv.setImageResource(selectedCategory.category_icon);

                    // Now you can use the categoryId as needed
                    // For example, create an item with the selected category

                }
            }
        });
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

    private void addFirebaseChildListener() {
        categoryService.addChildEventListener(new FirebaseChildEventListenerCallback<DataSnapshot>() {
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Category category = snapshot.getValue(Category.class);
                if (category != null) {
                    categories.add(category);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle changes if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle moves if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = -1;
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).category_id.equals(snapshot.getKey())) {
                        index = i;
                        break;
                    }
                }
                if (index !=-1) {
                    categories.remove(index);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}