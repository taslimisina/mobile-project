package ir.sharif.mobile.project.ui.todo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import ir.sharif.mobile.project.MainActivity;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Todo;
import ir.sharif.mobile.project.ui.repository.RepositoryHolder;
import ir.sharif.mobile.project.ui.utils.RecyclerItemTouchHelper;

public class TodoFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

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
        adaptor = new TodoViewAdaptor(todoList, handler, getContext(), view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adaptor);

        // adding item touch helper
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof TodoViewAdaptor.TodoViewHolder) {
            // get the removed item name to display it in snack bar
            String name = todoList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Todo deletedTask = todoList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adaptor.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(getView(), name + " removed from To Do!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    adaptor.restoreItem(deletedTask, deletedIndex);
                }
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        Message message = new Message();
                        message.what = TodoViewHandler.DELETE_TASK;
                        message.obj = deletedTask;
                        handler.sendMessage(message);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}