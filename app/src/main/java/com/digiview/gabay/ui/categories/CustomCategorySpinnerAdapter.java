package com.digiview.gabay.ui.categories;

// CustomSpinnerAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Category;

import java.util.List;

public class CustomCategorySpinnerAdapter extends ArrayAdapter<Category> {

    private Context context;
    private List<Category> categories;

    public CustomCategorySpinnerAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_category);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, R.layout.spinner_category);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent, false);
        }

        Category category = categories.get(position);

        ImageView icon = view.findViewById(R.id.category_icon);
        TextView name = view.findViewById(R.id.category_name);

        icon.setImageResource(category.category_icon);
        name.setText(category.category_name);

        return view;
    }
}
