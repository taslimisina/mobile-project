package ir.sharif.mobile.project.ui.todo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Todo;

public class TodoViewAdaptor extends RecyclerView.Adapter<TodoViewAdaptor.TodoViewHolder> {

    private List<Todo> todos;
    private final TodoViewHandler todoViewHandler;
    private final Context context;

    public TodoViewAdaptor(List<Todo> todos, TodoViewHandler todoViewHandler, Context context) {
        this.todos = todos;
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
        final Todo todo = todos.get(position);

        holder.viewBackground.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("todo", todo);
            Navigation.findNavController(v.getRootView().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editTodoFragment, bundle);
        });

        //Todo complete function
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout viewBackground;
        //Todo other fields

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.viewBackground = itemView.findViewById(R.id.item_background);
        }
    }
}
