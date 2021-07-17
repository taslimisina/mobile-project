package ir.sharif.mobile.project.ui.habits;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import ir.sharif.mobile.project.R;

import java.util.List;

public class TaskViewAdaptor extends RecyclerView.Adapter<TaskViewAdaptor.TaskViewHolder> {

    private List<Task> tasks;
    private final TaskViewHandler taskViewHandler;
//    private final Context context;

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        public final TextView title;
        public final TextView description;
        public final TextView reward;
//        public final View view;
        public final Button actionButton;
        public RelativeLayout viewBackground, viewForeground;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
//            this.view = itemView;
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            reward = itemView.findViewById(R.id.item_reward);
            actionButton = itemView.findViewById(R.id.item_action_button);
            viewBackground = itemView.findViewById(R.id.item_background);
            viewForeground = itemView.findViewById(R.id.item_foreground);
        }
    }

    public TaskViewAdaptor(List<Task> tasks, TaskViewHandler taskViewHandler) {
        this.tasks = tasks;
        this.taskViewHandler = taskViewHandler;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task, parent, false);
        view.findViewById(R.id.item_background).setOnClickListener(v -> {
            Log.d("TaskViewHolder", "OnClicked");
            Navigation.findNavController(v.getRootView().findViewById(R.id.fragment)).navigate(R.id.action_mainFragment_to_habitEditFragment); //todo pass args
        });
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        final Task task = tasks.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.reward.setText(String.valueOf(task.getReward()));
        // TODO: change button color for negative rewards

        holder.itemView.setOnClickListener(v -> {
            // TODO: go to edit fragment
        });

        holder.actionButton.setOnClickListener(v -> {
            // TODO: add reward
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void removeItem(int position) {
        tasks.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    public void restoreItem(Task task, int position) {
        tasks.add(position, task);
        // notify task added by position
        notifyItemInserted(position);
    }
}
