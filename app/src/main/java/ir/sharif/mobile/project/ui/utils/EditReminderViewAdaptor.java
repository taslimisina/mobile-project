package ir.sharif.mobile.project.ui.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Reminder;

public class EditReminderViewAdaptor extends RecyclerView.Adapter<EditReminderViewAdaptor.ReminderViewHolder> {

    private List<Reminder> reminders;
    private Context context;

    public class ReminderViewHolder extends RecyclerView.ViewHolder {

        public final TextView time;
        public final ImageButton deleteButton;
        public final MaterialCardView cardView;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.reminder_time);
            deleteButton = itemView.findViewById(R.id.remove_item_button);
            cardView = itemView.findViewById(R.id.reminder_item);
        }
    }

    public EditReminderViewAdaptor(List<Reminder> reminders, Context context) {
        this.reminders = reminders;
        this.context = context;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_edit_reminder_item, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        final Reminder reminder = reminders.get(position);
        // TODO: set time
//        Calendar currentTime = Calendar.getInstance();
//        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = currentTime.get(Calendar.MINUTE);

        holder.deleteButton.setOnClickListener(v -> {
            int removedItemPosition = reminders.indexOf(reminder);
            removeItem(removedItemPosition);
        });

        holder.cardView.setOnClickListener(v -> {
            String timeString = holder.time.getText().toString();
            String[] timeParts = timeString.split(":");
            int hh = Integer.parseInt(timeParts[0]);
            int mm = Integer.parseInt(timeParts[1]);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    holder.time.setText(context.getString(R.string.time,  selectedHour, selectedMinute));
                }
            }, hh, mm, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
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

    public void addItem(Reminder reminder, int position) {
        reminders.add(position, reminder);
        notifyItemInserted(position);
    }

}