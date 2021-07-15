package ir.sharif.mobile.project.ui.habits;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ir.sharif.mobile.project.MainActivity;
import ir.sharif.mobile.project.R;

public class HabitsFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Task> habits;
    private TaskViewAdaptor mAdapter;
    private TaskViewHandler handler;
    private TaskDbHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        dbHelper = new TaskDbHelper(getContext());
//        dbHelper.insert(new Task("Task 1", "This is a static test task!", 50));
//        dbHelper.insert(new Task("Task 2", "This is a static test task!", 150));
//        dbHelper.insert(new Task("Task 3", "This is a static test task!", -50));
//        dbHelper.insert(new Task("Task 4", "This is a static test task!", 220));
        handler = new TaskViewHandler(dbHelper);
        habits = dbHelper.getAllMatched(null);
        mAdapter = new TaskViewAdaptor(habits, handler);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return view;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof TaskViewAdaptor.TaskViewHolder) {
            // get the removed item name to display it in snack bar
            String name = habits.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Task deletedTask = habits.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(getView(), name + " removed from habits!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedTask, deletedIndex);
                }
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        Message message = new Message();
                        message.what = TaskViewHandler.DELETE_DATA;
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