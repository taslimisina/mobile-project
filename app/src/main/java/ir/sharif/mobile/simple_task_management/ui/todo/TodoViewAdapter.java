package ir.sharif.mobile.simple_task_management.ui.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import ir.sharif.mobile.simple_task_management.Executor;
import ir.sharif.mobile.simple_task_management.R;
import ir.sharif.mobile.simple_task_management.model.ChecklistItem;
import ir.sharif.mobile.simple_task_management.model.Todo;
import ir.sharif.mobile.simple_task_management.util.DateUtil;
import ir.sharif.mobile.simple_task_management.ui.utils.TwoLayerView;

public class TodoViewAdapter extends RecyclerView.Adapter<TodoViewAdapter.TodoViewHolder> {

    private List<Todo> todoList;
    private final Context context;
    private final View rootView;

    public TodoViewAdapter(Context context, View rootView) {
        this.todoList = new ArrayList<>();
        this.context = context;
        this.rootView = rootView;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_todo, parent, false);
        return new TodoViewAdapter.TodoViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        final Todo todo = todoList.get(position);
        holder.title.setText(todo.getTitle());
        holder.description.setText(todo.getDescription());
        holder.reward.setText(String.valueOf(todo.getReward()));

        // Init checklist
        holder.checklist.removeAllViews();
        for (ChecklistItem item : todo.getChecklistItems()) {
            View checklistItemView = LayoutInflater.from(context).inflate(R.layout.layout_checklist_item, (ViewGroup)holder.checklist, false);
            ((TextView)checklistItemView.findViewById(R.id.checklist_item_title)).setText(item.getName());
            CheckBox itemCheckBox = checklistItemView.findViewById(R.id.checklist_item_box);
            itemCheckBox.setChecked(item.isChecked());
            itemCheckBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {item.setChecked(isChecked);
                Executor.getInstance().saveChecklistItem(item);
            });
            holder.checklist.addView(checklistItemView);
        }

        holder.checklist.setVisibility(View.GONE);
        // Init expand button
        if (todo.getChecklistItems().size() == 0) {
            holder.expandButton.setVisibility(View.GONE);
        } else {
            holder.expandButton.setVisibility(View.VISIBLE);
            holder.expandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
            holder.expandButton.setOnClickListener(v -> {
                if (holder.checklist.getVisibility() == View.GONE) {
                    holder.expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    holder.checklist.setVisibility(View.VISIBLE);
                } else {
                    holder.expandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    holder.checklist.setVisibility(View.GONE);
                }
            });
        }

        // Set due date
        if (todo.getDueDate() != null) {
            holder.dueDate.setText(DateUtil.formatDate(todo.getDueDate(), DateUtil.SHORT));
            Date today = Calendar.getInstance().getTime();
            if (todo.getDueDate().before(today)) {
                holder.dueDate.setTextColor(context.getResources().getColor(R.color.red_icon));
            }
        } else {
            holder.dueDateIcon.setVisibility(View.GONE);
            holder.dueDate.setVisibility(View.GONE);
        }

        // Checked listener
        holder.checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                checkTodo(holder);
            }
        });


        holder.viewBackground.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("todo", todo);
            Navigation.findNavController(v.getRootView().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editTodoFragment, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void addAll(List<Todo> list) {
        for (Todo todo : list)
            if (!todoList.contains(todo))
                todoList.add(todo);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        todoList.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    public void restoreItem(Todo todo, int position) {
        todoList.add(position, todo);
        // notify task added by position
        notifyItemInserted(position);
    }

    public Todo getItem(int position) {
        return todoList.get(position);
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder implements TwoLayerView {

        public final TextView title;
        public final TextView description;
        public final TextView reward;
        public final CheckBox checkBox;
        public final RelativeLayout viewBackground, viewForeground;
        public final LinearLayout checklist;
        public final ImageButton expandButton;
        public final TextView dueDate;
        public final ImageView dueDateIcon;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            reward = itemView.findViewById(R.id.item_reward);
            checkBox = itemView.findViewById(R.id.item_checkbox);
            viewBackground = itemView.findViewById(R.id.item_background);
            viewForeground = itemView.findViewById(R.id.item_foreground);
            checklist = itemView.findViewById(R.id.checklist);
            expandButton = itemView.findViewById(R.id.expand_button);
            dueDate = itemView.findViewById(R.id.due_date);
            dueDateIcon = itemView.findViewById(R.id.due_logo);
        }

        @Override
        public View getViewForeground() {
            return this.viewForeground;
        }
    }

    void checkTodo(TodoViewHolder holder) {
        String name = todoList.get(holder.getAdapterPosition()).getTitle();

        // backup of removed item for undo purpose
        final Todo doneTask = todoList.get(holder.getAdapterPosition());
        final int doneIndex = holder.getAdapterPosition();

        // add reward
        Executor.getInstance().addCoin(doneTask.getReward());

        // remove the item from recycler view
        removeItem(holder.getAdapterPosition());

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar.make(this.rootView, name + " is done!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", view -> {
            // undo is selected, restore the deleted item
            snackbar.setAction("UNDO", v -> {});
            holder.checkBox.setChecked(false);
            restoreItem(doneTask, doneIndex);
            Executor.getInstance().undoCoin();
        });
        snackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT)
                    Executor.getInstance().deleteTask(doneTask);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void clearList() {
        todoList.clear();
    }
}
