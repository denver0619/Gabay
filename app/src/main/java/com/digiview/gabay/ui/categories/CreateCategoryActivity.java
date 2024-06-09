package com.digiview.gabay.ui.categories;


import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.gabay.R;

import java.util.ArrayList;
import java.util.List;


public class CreateCategoryActivity extends AppCompatActivity {


    private RecyclerView iconRecyclerView;
    private TextView selectedIconTextView;
    private IconAdapter adapter;
    private ImageView outputIcon;

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

        modifyActionBar();


        iconRecyclerView = findViewById(R.id.iconRecyclerView);
        selectedIconTextView = findViewById(R.id.selectedIconTextView); // Initialize the TextView
        iconRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        outputIcon = findViewById(R.id.outputIcon);


        List<Icon> iconList = generateDummyIcons();
        adapter = new IconAdapter(iconList, new IconAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int iconId) {
                // Update the TextView with the ID of the selected icon
                selectedIconTextView.setVisibility(View.VISIBLE);
                selectedIconTextView.setText("Selected Icon ID: " + iconId);


                outputIcon.setImageResource(iconId);

                // Insert Action Upon Item Click

            }
        });
        iconRecyclerView.setAdapter(adapter);



    }

    private List<Icon> generateDummyIcons() {
        List<Icon> iconList = new ArrayList<>();
        // Add your icon resources here
        iconList.add(new Icon(R.drawable.ic_accomodation_101));
        iconList.add(new Icon(R.drawable.ic_accomodation_102));
        iconList.add(new Icon(R.drawable.ic_accomodation_103));
        iconList.add(new Icon(R.drawable.ic_accomodation_104));
        iconList.add(new Icon(R.drawable.ic_accomodation_105));

        iconList.add(new Icon(R.drawable.ic_attraction_101));
        iconList.add(new Icon(R.drawable.ic_attraction_102));
        iconList.add(new Icon(R.drawable.ic_attraction_103));
        iconList.add(new Icon(R.drawable.ic_attraction_104));
        iconList.add(new Icon(R.drawable.ic_attraction_105));

        iconList.add(new Icon(R.drawable.ic_clothes_101));
        iconList.add(new Icon(R.drawable.ic_clothes_102));
        iconList.add(new Icon(R.drawable.ic_clothes_103));
        iconList.add(new Icon(R.drawable.ic_clothes_104));
        iconList.add(new Icon(R.drawable.ic_clothes_105));

        iconList.add(new Icon(R.drawable.ic_food_101));
        iconList.add(new Icon(R.drawable.ic_food_102));
        iconList.add(new Icon(R.drawable.ic_food_103));
        iconList.add(new Icon(R.drawable.ic_food_104));
        iconList.add(new Icon(R.drawable.ic_food_105));

        iconList.add(new Icon(R.drawable.ic_transportation_101));
        iconList.add(new Icon(R.drawable.ic_transportation_102));
        iconList.add(new Icon(R.drawable.ic_transportation_103));
        iconList.add(new Icon(R.drawable.ic_transportation_104));
        iconList.add(new Icon(R.drawable.ic_transportation_105));


        // Add more icons as needed
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


}

