package ir.sharif.mobile.project.ui.habits;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Habit;
import ir.sharif.mobile.project.ui.utils.TwoLayerView;

import java.util.List;

public class HabitViewAdaptor extends RecyclerView.Adapter<HabitViewAdaptor.HabitViewHolder> {

    private List<Habit> habits;
    private final HabitViewHandler habitViewHandler;
    private final Context context;

    public class HabitViewHolder extends RecyclerView.ViewHolder implements TwoLayerView {

        public final TextView title;
        public final TextView description;
        public final TextView reward;
        public final ImageButton actionButton;
        public RelativeLayout viewBackground, viewForeground;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            reward = itemView.findViewById(R.id.item_reward);
            actionButton = itemView.findViewById(R.id.item_action_button);
            viewBackground = itemView.findViewById(R.id.item_background);
            viewForeground = itemView.findViewById(R.id.item_foreground);
        }

        @Override
        public View getViewForeground() {
            return viewForeground;
        }
    }

    public HabitViewAdaptor(List<Habit> habits, HabitViewHandler habitViewHandler, Context context) {
        this.habits = habits;
        this.habitViewHandler = habitViewHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_habits, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        final Habit habit = habits.get(position);
        holder.title.setText(habit.getTitle());
        holder.description.setText(habit.getDescription());
        holder.reward.setText(String.valueOf(habit.getReward()));

        holder.viewBackground.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("task", habit);
            Navigation.findNavController(v.getRootView().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editHabitFragment, bundle);
        });

        if (habit.getReward() < 0) {
            holder.actionButton.setImageResource(R.drawable.ic_minus_red_48);
            holder.actionButton.setBackgroundColor(context.getResources().getColor(R.color.red_back));
        } else {
            holder.actionButton.setImageResource(R.drawable.ic_plus_green_48);
            holder.actionButton.setBackgroundColor(context.getResources().getColor(R.color.green_back));
        }

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("habit", habit);
            Navigation.findNavController(v.getRootView().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editHabitFragment, bundle);
        });

        holder.actionButton.setOnClickListener(v -> {
            Message message = new Message();
            message.what = HabitViewHandler.DONE_TASK;
            message.obj = habit;
            habitViewHandler.sendMessage(message);
        });

    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public void removeItem(int position) {
        habits.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    public void restoreItem(Habit habit, int position) {
        habits.add(position, habit);
        // notify task added by position
        notifyItemInserted(position);
    }
}
