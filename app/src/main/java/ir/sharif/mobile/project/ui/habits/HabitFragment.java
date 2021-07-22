package ir.sharif.mobile.project.ui.habits;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import ir.sharif.mobile.project.Executor;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Habit;
import ir.sharif.mobile.project.ui.repository.TaskRepository;
import ir.sharif.mobile.project.ui.utils.RecyclerItemTouchHelper;

public class HabitFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private HabitViewAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new HabitViewAdapter(getContext());
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
    public void onStart() {
        super.onStart();
        Executor.getInstance().setHandler(new HabitViewHandler(this));
        Executor.getInstance().loadTasks(TaskRepository.TaskType.HABIT);
        getActivity().findViewById(R.id.new_button).setOnClickListener(v -> {
            Navigation.findNavController(getActivity().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editHabitFragment);
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof HabitViewAdapter.HabitViewHolder) {
            // get the removed item name to display it in snack bar
            String name = mAdapter.getItem(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Habit deletedTask = mAdapter.getItem(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(getView(), name + " removed from Habits!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    snackbar.setAction("UNDO", v -> {});
                    mAdapter.restoreItem(deletedTask, deletedIndex);
                }
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT)
                        Executor.getInstance().deleteTask(deletedTask);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public HabitViewAdapter getAdapter() {
        return mAdapter;
    }
}