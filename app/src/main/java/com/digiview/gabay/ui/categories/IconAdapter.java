package com.digiview.gabay.ui.categories;

import com.digiview.gabay.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder> {

    private List<Icon> iconList;
    private int selectedPosition = RecyclerView.NO_POSITION; // Initially no item is selected
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int iconId);
    }

    public IconAdapter(List<Icon> iconList, OnItemClickListener listener) {
        this.iconList = iconList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        Icon icon = iconList.get(position);
        holder.iconImageView.setImageResource(icon.getIconResource());

        // Highlight the selected icon
        holder.itemView.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the selected position
                notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);

                if (listener != null) {
                    listener.onItemClick(icon.getIconResource());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    static class IconViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImageView;

        IconViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
        }
    }
}






