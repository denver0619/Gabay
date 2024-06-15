package com.digiview.gabay.ui.categories;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Locale;

public class EditCategoryActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener{

    private RecyclerView iconRecyclerView;
    private TextView selectedIconTextView;
    private IconAdapter adapter;
    private CategoryService categoryService;
    private ImageView outputIcon;
    private EditText inputCategoryName;
    private Button saveButton;
    private  String categoryID;
    private  String categoryName;
    private  Integer categoryIcon;
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tts = new TextToSpeech(this, this);
        tts.setLanguage(Locale.US);

        // Extract intent extras to populate UI fields
        Intent intent = getIntent();
        categoryID = intent.getStringExtra("CATEGORY_ID");
        categoryName = intent.getStringExtra("CATEGORY_NAME");
        categoryIcon = intent.getIntExtra("CATEGORY_ICON", -1);

        // Customize action bar and status bar appearance
        modifyActionBar();

        // Initialize CategoryService for data operations
        categoryService = CategoryService.getInstance();

        // Initialize RecyclerView for icons
        iconRecyclerView = findViewById(R.id.iconRecyclerView);
        iconRecyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        // Initialize views for category details
        outputIcon = findViewById(R.id.outputIcon);
        inputCategoryName = findViewById(R.id.EditCategory_InputCategoryName);
        selectedIconTextView = findViewById(R.id.selectedIconTextView);

        // Initialize and set up Save button
        saveButton = findViewById(R.id.button_EditCategory);
        saveButton.setOnClickListener(this);

        // Set the received data to input fields
        if (categoryName != null) {
            inputCategoryName.setText(categoryName);
        }
        if (categoryIcon != -1) {
            outputIcon.setImageResource(categoryIcon);
            outputIcon.setVisibility(View.GONE);

            selectedIconTextView.setVisibility(View.GONE);
            selectedIconTextView.setText(String.valueOf(categoryIcon));
        } else {
            selectedIconTextView.setText(""); // Set to an empty string to avoid null issues
        }

        // Generate icons and initialize adapter
        List<Icon> iconList = generateDummyIcons();
        int initialSelectedPosition = getPositionForIcon(categoryIcon, iconList);
        adapter = new IconAdapter(iconList, new IconAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int iconId) {
                // Update the TextView with the ID of the selected icon
                selectedIconTextView.setVisibility(View.GONE);
                selectedIconTextView.setText(String.valueOf(iconId));
                outputIcon.setImageResource(iconId);
                outputIcon.setVisibility(View.GONE);
            }
        }, initialSelectedPosition);
        iconRecyclerView.setAdapter(adapter);


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

    @Override
    public void onClick(View v) {
        int currentID = v.getId();
        if (currentID == R.id.button_EditCategory) {
            onCategoryEdit();
            this.finish();
        }
    }

    private void onCategoryEdit() {
        // Perform update operation for the category
        Category category = new Category();
        category.category_id = categoryID;
        category.category_name = inputCategoryName.getText().toString();

        // Parse and set selected icon from TextView
        String iconText = selectedIconTextView.getText().toString();
        if (!iconText.isEmpty()) {
            category.category_icon = Integer.parseInt(iconText);
        } else {
            category.category_icon = -1;
        }

        categoryService.editCategory(category); // Call service method to update category
    }

    @Override
    public void onInit(int status) {

    }
}