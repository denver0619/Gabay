package com.digiview.gabay.ui.categories;


import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.res.TypedArray;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Category;
import com.digiview.gabay.services.CategoryService;

import java.util.ArrayList;
import java.util.List;


public class CreateCategoryActivity extends AppCompatActivity implements View.OnClickListener{

    // UI components
    private RecyclerView iconRecyclerView;
    private TextView selectedIconTextView;
    private ImageView outputIcon;
    private EditText categoryName;
    private Button saveButton;

    // Other variables
    private IconAdapter adapter;
    private CategoryService categoryService;
    private Integer categoryIcon; // Placeholder for category icon resource ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Customize action bar and status bar appearance
        modifyActionBar();

        categoryService = CategoryService.getInstance();


        // Initialize UI components
        iconRecyclerView = findViewById(R.id.iconRecyclerView);
        outputIcon = findViewById(R.id.outputIcon);
        categoryName = findViewById(R.id.CreateCategory_InputCategoryName);
        selectedIconTextView = findViewById(R.id.selectedIconTextView);
        saveButton = findViewById(R.id.button_CreateCategory);

        // Initial icon resource ID for selection
        categoryIcon = 2131230892;

        // Display initial icon in outputIcon ImageView
        outputIcon.setImageResource(categoryIcon);

        // Display initial icon ID in selectedIconTextView
        selectedIconTextView.setText(String.valueOf(categoryIcon));

        // Setup RecyclerView for icons
        List<Icon> iconList = generateDummyIcons();
        int initialSelectedPosition = getPositionForIcon(categoryIcon, iconList);
        adapter = new IconAdapter(iconList, new IconAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int iconId) {
                // Update the TextView with the ID of the selected icon
                selectedIconTextView.setVisibility(View.VISIBLE);
                selectedIconTextView.setText(String.valueOf(iconId));


                outputIcon.setImageResource(iconId);

                // Insert Action Upon Item Click

            }
        }, initialSelectedPosition);
        iconRecyclerView.setAdapter(adapter);

        saveButton.setOnClickListener(this);

    }

    private int getPositionForIcon(int iconId, List<Icon> iconList) {
        for (int i = 0; i < iconList.size(); i++) {
            if (iconList.get(i).getIconResource() == iconId) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    private List<Icon> generateDummyIcons() {
        List<Icon> iconList = new ArrayList<>();
        TypedArray iconsArray = getResources().obtainTypedArray(R.array.icon_resources);

        for (int i = 0; i < iconsArray.length(); i++) {
            int iconResource = iconsArray.getResourceId(i, -1);
            if (iconResource != -1) {
                iconList.add(new Icon(iconResource));
            }
        }
        iconsArray.recycle();

        return iconList;

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
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close); // Ensure you have an ic_close drawable
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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


    @Override
    public void onClick(View v) {
        int currentID = v.getId();
        if (currentID == R.id.button_CreateCategory) {
            onCategorySave();
            this.finish();
        }
    }

    private void onCategorySave() {
        Category category = new Category();
        category.category_name = categoryName.getText().toString();
        category.category_icon = Integer.parseInt(selectedIconTextView.getText().toString());
        categoryService.createCategory(category);
    }


}

