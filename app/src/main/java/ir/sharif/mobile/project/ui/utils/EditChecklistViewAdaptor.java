package ir.sharif.mobile.project.ui.utils;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.ChecklistItem;


public class EditChecklistViewAdaptor extends RecyclerView.Adapter<EditChecklistViewAdaptor.ChecklistItemViewHolder> {

    private List<ChecklistItem> items;

    public class ChecklistItemViewHolder extends RecyclerView.ViewHolder {

        public final TextInputEditText name;
        public final ImageButton deleteButton;

        public ChecklistItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_title);
            deleteButton = itemView.findViewById(R.id.remove_item_button);
        }
    }

    public EditChecklistViewAdaptor(List<ChecklistItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ChecklistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_edit_checklist_item, parent, false);
        return new ChecklistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistItemViewHolder holder, int position) {
        final ChecklistItem item = items.get(position);
        holder.name.setText(item.getName());

        holder.deleteButton.setOnClickListener(v -> {
            int removedItemPosition = items.indexOf(item);
            removeItem(removedItemPosition);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeItem(int position) {
        items.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    public void addItem(ChecklistItem item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }
}
