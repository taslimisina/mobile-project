package ir.sharif.mobile.project.ui.todo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Todo;

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

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        final Todo todo = todoList.get(position);
        holder.title.setText(todo.getTitle());
        holder.description.setText(todo.getDescription());
        holder.reward.setText(String.valueOf(todo.getReward()));

        // TODO: 7/20/21 show checklist
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
        public final AppCompatCheckBox checkBox;
        public RelativeLayout viewBackground, viewForeground;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            viewBackground = itemView.findViewById(R.id.item_background);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            reward = itemView.findViewById(R.id.item_reward);
            checkBox = itemView.findViewById(R.id.item_checkbox);
            viewBackground = itemView.findViewById(R.id.item_background);
            viewForeground = itemView.findViewById(R.id.item_foreground);
        }
    }
}
