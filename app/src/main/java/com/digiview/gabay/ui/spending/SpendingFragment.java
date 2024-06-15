package com.digiview.gabay.ui.spending;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Item;
import com.digiview.gabay.services.ItemService;
import com.digiview.gabay.services.FirebaseChildEventListenerCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class SpendingFragment extends Fragment {

    private SpendingAdapter spendingAdapter;
    private RecyclerView recyclerView;

    public SpendingFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and Adapter
        recyclerView = view.findViewById(R.id.Spending_RecyclerView);
        spendingAdapter = new SpendingAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(spendingAdapter);

        // Load data from ItemService
        loadRecentItems();

        // Hide the FloatingActionButton
        FloatingActionButton fab = getActivity().findViewById(R.id.main_fab);
        fab.hide();
    }

    private void loadRecentItems() {
        ItemService itemService = ItemService.getInstance();
        itemService.addChildEventListenerForRecentAddedItem(new FirebaseChildEventListenerCallback<DataSnapshot>() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Item item = snapshot.getValue(Item.class);
                if (item != null) {
                    spendingAdapter.addItem(item);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle child changed if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle child removed if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle child moved if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = getActivity().findViewById(R.id.main_fab);
        fab.hide(); // Ensure the FAB is hidden
    }
}
