package com.digiview.gabay.ui.spending;

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
import com.digiview.gabay.domain.entities.Item;
import com.digiview.gabay.services.CategoryService;
import com.digiview.gabay.services.FirebaseValueEventListenerCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.SpendingViewHolder> {

    private List<Item> items;
    private Context context;
    private CategoryService categoryService;

    public SpendingAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
        this.categoryService = CategoryService.getInstance();
    }

    @NonNull
    @Override
    public SpendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spending_recycler_view_row, parent, false);
        return new SpendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingViewHolder holder, int position) {
        Item item = items.get(position);

        // Set item data
        holder.expenseName.setText(item.item_name);
        holder.expenseAmount.setText("₱ " + item.item_cost);

        // Fetch and set category data
        categoryService.addValueEventListener(item.category_id, new FirebaseValueEventListenerCallback<DataSnapshot>() {
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category category = snapshot.getValue(Category.class);
                if (category != null) {
                    holder.expenseIcon.setImageResource(category.category_icon);
                    holder.expenseCategory.setText(category.category_name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items = items;
        Collections.reverse(this.items); // Reverse the list to get the most recently added items first
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        this.items.add(0, item); // Add item to the top of the list
        notifyItemInserted(0);
    }

    public double getTotalCost() {
        double totalCost = 0.00;
        for (Item item : items) {
            totalCost += item.item_cost;
        }
        return totalCost;
    }

    static class SpendingViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseCategory, expenseAmount;
        ImageView expenseIcon;

        SpendingViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.Spending_ItemList_Name);
            expenseCategory = itemView.findViewById(R.id.Spending_ItemList_CategoryName);
            expenseAmount = itemView.findViewById(R.id.Spending_ExpenseMoney);
            expenseIcon = itemView.findViewById(R.id.Spending_ExpenseCategoryIcon);
        }
    }
}
