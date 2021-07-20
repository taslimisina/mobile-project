package ir.sharif.mobile.project.ui.todo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import ir.sharif.mobile.project.MainActivity;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Todo;
import ir.sharif.mobile.project.ui.utils.RecyclerItemTouchHelper;

public class TodoFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Todo> todoList;
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
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        init();
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

}