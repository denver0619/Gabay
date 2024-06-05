package com.digiview.gabay.ui.spending;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiview.gabay.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SpendingFragment extends Fragment {

    public SpendingFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spending, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = getActivity().findViewById(R.id.main_fab);
        fab.hide(); // Ensure the FAB is hidden
    }
}