package com.digiview.gabay;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.digiview.gabay.databinding.ActivityMainBinding;
import com.digiview.gabay.ui.categories.CategoriesFragment;
import com.digiview.gabay.ui.spending.SpendingFragment;
import com.digiview.gabay.ui.trips.TripsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add Logo to ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_bus_solid);
        setContentView(R.layout.activity_main);

        // Set Bottom Navigation
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setElevation(100f);

        replaceFragment(new SpendingFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_spending:
                    replaceFragment(new SpendingFragment());
                    break;
                case R.id.navigation_trips:
                    replaceFragment(new TripsFragment());
                    break;
                case R.id.navigation_categories:
                    replaceFragment(new CategoriesFragment());
                    break;
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutFragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}