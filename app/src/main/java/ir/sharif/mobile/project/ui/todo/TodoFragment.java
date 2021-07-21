package ir.sharif.mobile.project.ui.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.sharif.mobile.project.MainActivity;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Todo;
import ir.sharif.mobile.project.ui.repository.RepositoryHolder;
import ir.sharif.mobile.project.ui.utils.RecyclerItemTouchHelper;

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
        List<ChecklistItem> list = new ArrayList<>();
        list.add(new ChecklistItem().setName("One"));
        list.add(new ChecklistItem().setName("Two"));
        todo.setDueDate(Calendar.getInstance().getTime());
        todo.setChecklistItems(list);
        todo.setReminders(new ArrayList<>());
//        todoList = new ArrayList<>();
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
        todoList.clear();
        todoList.addAll(RepositoryHolder.getTaskRepository().findAllTodo());
//        adaptor.notifyDataSetChanged();

        getActivity().findViewById(R.id.new_button).setOnClickListener(v -> {
            Navigation.findNavController(getActivity().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editTodoFragment);
        });
    }
}