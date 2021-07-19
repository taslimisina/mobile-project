package ir.sharif.mobile.project.ui.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Reminder;

public class ReminderViewAdaptor extends RecyclerView.Adapter<ReminderViewAdaptor.ReminderViewHolder> {

    private List<Reminder> reminders;

    public class ReminderViewHolder extends RecyclerView.ViewHolder {

        public final TextView time;
        public final ImageButton deleteButton;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.reminder_time);
            deleteButton = itemView.findViewById(R.id.remove_item_button);

        }
    }

    public ReminderViewAdaptor(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_habits, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        final Reminder reminder = reminders.get(position);
        // TODO: set clock value
//        holder.time.setText();

        holder.deleteButton.setOnClickListener(v -> {
            removeItem(position);
        });

    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void removeItem(int position) {
        reminders.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    public void addItem() {
        reminders.add(new Reminder());
        notifyItemInserted(getItemCount() - 1);
    }

}
