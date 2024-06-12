package com.digiview.gabay.ui.categories;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Category;
import com.digiview.gabay.domain.entities.Trip;
import com.digiview.gabay.services.CategoryService;
import com.digiview.gabay.services.FirebaseChildEventListenerCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;
    private List<Category> categories;

    private CategoryService categoryService;


    public CategoriesFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.Categories_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(getContext(), categories);

        categoryService = CategoryService.getInstance();
        addFirebaseChildListener();

        recyclerView.setAdapter(categoriesAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = getActivity().findViewById(R.id.main_fab);
        fab.show(); // Make the FAB visible

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle FAB click for TripsFragment
                 Intent intent = new Intent(getActivity(), CreateCategoryActivity.class);
                 startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        FloatingActionButton fab = getActivity().findViewById(R.id.main_fab);
        fab.hide(); // Hide the FAB when the fragment is not visible
    }

    private void addFirebaseChildListener() {
        categoryService.addChildEventListener(new FirebaseChildEventListenerCallback<DataSnapshot>() {
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Category category = snapshot.getValue(Category.class);
                // Toast.makeText(getActivity(), trip.trip_name, Toast.LENGTH_SHORT).show();
                categories.add(0, category);
                categoriesAdapter.notifyItemInserted(0);
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
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).category_id.equals(snapshot.getKey())) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    categories.remove(index);
                    categoriesAdapter.notifyItemRemoved(index);
                }
            }
        });

    }
}
