package ir.sharif.mobile.project.ui.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Todo;
import ir.sharif.mobile.project.ui.model.utils.DateUtil;

public class TodoViewAdaptor extends RecyclerView.Adapter<TodoViewAdaptor.TodoViewHolder> {

    private List<Todo> todoList;
    private final TodoViewHandler todoViewHandler;
    private final Context context;

    public TodoViewAdaptor(List<Todo> todoList, TodoViewHandler todoViewHandler, Context context) {
        this.todoList = todoList;
        this.todoViewHandler = todoViewHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_todo, parent, false);
        return new TodoViewAdaptor.TodoViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        final Todo todo = todoList.get(position);
        holder.title.setText(todo.getTitle());
        holder.description.setText(todo.getDescription());
        holder.reward.setText(String.valueOf(todo.getReward()));

        // Init checklist
        for (ChecklistItem item : todo.getChecklistItems()) {
            View checklistItemView = LayoutInflater.from(context).inflate(R.layout.layout_checklist_item, (ViewGroup)holder.checklist, false);
            ((TextView)checklistItemView.findViewById(R.id.checklist_item_title)).setText(item.getName());
            holder.checklist.addView(checklistItemView);
        }

        holder.checklist.setVisibility(View.GONE);
        // Init expand button
        if (todo.getChecklistItems().size() == 0) {
            holder.expandButton.setVisibility(View.GONE);
        } else {
            holder.expandButton.setVisibility(View.VISIBLE);
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
            if (todo.getDueDate().after(today)) {
                holder.dueDate.setTextColor(context.getResources().getColor(R.color.red_icon));
            }
        } else {
            holder.dueDateIcon.setVisibility(View.GONE);
            holder.dueDate.setVisibility(View.GONE);
        }

        // TODO: 7/20/21 checkBox click listener

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

    public class TodoViewHolder extends RecyclerView.ViewHolder {

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
    }
}
