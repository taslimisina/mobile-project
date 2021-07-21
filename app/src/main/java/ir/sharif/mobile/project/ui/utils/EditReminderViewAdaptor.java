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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Reminder;

public class EditReminderViewAdaptor extends RecyclerView.Adapter<EditReminderViewAdaptor.ReminderViewHolder> {

    private List<Reminder> reminders;
    private Context context;
    private List<Long> toBeDeletedReminders = new ArrayList<>();

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
        Calendar time = Calendar.getInstance();
        time.setTime(reminder.getTime());
        holder.time.setText(context.getString(R.string.time, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE)));

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
            mTimePicker = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            holder.time.setText(context.getString(R.string.time, selectedHour, selectedMinute));

                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(new Date());
                            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                            calendar.set(Calendar.MINUTE, selectedMinute);
                            reminder.setTime(calendar.getTime());
                        }
                    }, hh, mm, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mTimePicker.show();
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void removeItem(int position) {
        toBeDeletedReminders.add(reminders.get(position).getId());
        reminders.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    public void addItem(Reminder reminder, int position) {
        reminders.add(position, reminder);
        notifyItemInserted(position);
    }

    public void clearToBeDeletedReminders() {
        toBeDeletedReminders.clear();
    }

    public List<Long> getToBeDeletedReminders() {
        return toBeDeletedReminders;
    }
}
