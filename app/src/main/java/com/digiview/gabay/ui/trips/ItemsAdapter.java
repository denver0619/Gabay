package com.digiview.gabay.ui.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.gabay.R;
import com.digiview.gabay.domain.entities.Category;
import com.digiview.gabay.domain.entities.Item;
import com.digiview.gabay.services.CategoryService;
import com.digiview.gabay.services.ItemService;
import com.digiview.gabay.services.FirebaseValueEventListenerCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {private final Context context;
    private List<Item> tripItems;
    private OnItemRemoveListener onItemRemoveListener;
    public CategoryService categoryService;
    private ItemService itemService;

    public interface OnItemRemoveListener {
        void onItemRemove(int position);
    }

    public ItemsAdapter(List<Item> tripItems, OnItemRemoveListener onItemRemoveListener, Context context) {
        this.tripItems = tripItems;
        this.onItemRemoveListener = onItemRemoveListener;
        this.context = context;
        this.itemService = ItemService.getInstance();

        categoryService = CategoryService.getInstance();

    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_detail_item_recycler_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item tripItem = tripItems.get(position);
        final Integer[] categoryIcon = new Integer[1];
        categoryService.addValueEventListener(tripItem.category_id, new FirebaseValueEventListenerCallback<DataSnapshot>() {
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category category = snapshot.getValue(Category.class);
                categoryIcon[0] = category.category_icon;
                holder.iconCategoryImageView.setImageResource(categoryIcon[0]);
            }
        });

        holder.nameCategoryTextView.setText(tripItem.item_name);
        holder.costItemTextView.setText(String.format("₱ %.2f", tripItem.item_cost));

        holder.removeButton.setOnClickListener(v -> {
            // Perform deletion when remove button is clicked
            onItemRemoveListener.onItemRemove(position);
        });

    }

    @Override
    public int getItemCount() {
        return tripItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView iconCategoryImageView;
        public TextView nameCategoryTextView;
        public TextView costItemTextView;
        public TextView itemIDDebug;

        public ImageButton removeButton;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            iconCategoryImageView = itemView.findViewById(R.id.TripDetails_ItemList_Icon);
            nameCategoryTextView = itemView.findViewById(R.id.TripDetails_ItemList_Name);
            costItemTextView = itemView.findViewById(R.id.TripDetails_ItemList_Cost);
            removeButton = itemView.findViewById(R.id.TripDetails_ItemList_Remove);
        }
    }
}
