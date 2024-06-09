package com.digiview.gabay.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Category;

import java.util.List;
class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>{

    List<Category> categoryList;
    private Context context;
    public CategoriesAdapter(Context context, List<Category> categoryList){
        this.categoryList = categoryList;
        this.context = context;
    }
    @NonNull
    @Override
    public CategoriesAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_recycler_view_row, parent, false);
        return new CategoriesAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.category_name);
        holder.categoryIcon.setImageResource(category.category_icon);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        ImageView categoryIcon;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.Categories_CategoryTitle);
            categoryIcon = itemView.findViewById(R.id.Categories_CategoryIcon);
        }
    }
}
