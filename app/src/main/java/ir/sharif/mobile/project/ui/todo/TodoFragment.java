package ir.sharif.mobile.project.ui.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.sharif.mobile.project.MainActivity;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Task;
import ir.sharif.mobile.project.ui.model.Todo;
import ir.sharif.mobile.project.ui.repository.RepositoryHolder;
import ir.sharif.mobile.project.ui.repository.TaskRepository;

public class TodoFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Todo> todoList = new ArrayList<>();
    private TodoViewAdaptor adaptor;
    private TodoViewHandler handler;

    private void init() {
        Todo todo = new Todo();
        todo.setTitle("Hello");
        todo.setDescription("a description");
        todo.setReward(20);
        todo.setChecklistItems(new ArrayList<>());
        todo.setReminders(new ArrayList<>());
        todoList = new ArrayList<>();
        todoList.add(todo);
        RepositoryHolder.getTaskRepository().save(todo);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

//        init();
        recyclerView = view.findViewById(R.id.recycler_view);
        handler = new TodoViewHandler();
        adaptor = new TodoViewAdaptor(todoList, handler, getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adaptor);

        // adding item touch helper
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TodoFragment", "onStart called!");
        TaskRepository taskRepository = RepositoryHolder.getTaskRepository();
        List<Task> todos = taskRepository.findAll(TaskRepository.TaskType.TODO);
        todoList.clear();
        for (Task task : todos) {
            todoList.add((Todo) task);
        }
        adaptor.notifyDataSetChanged();
        Log.d("TodoFragment", Integer.toString(todos.size()));
    }
}