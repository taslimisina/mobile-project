package ir.sharif.mobile.simple_task_management.ui.dailies;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.sharif.mobile.simple_task_management.Executor;
import ir.sharif.mobile.simple_task_management.R;
import ir.sharif.mobile.simple_task_management.model.ChecklistItem;
import ir.sharif.mobile.simple_task_management.model.Daily;
import ir.sharif.mobile.simple_task_management.repository.TaskRepository;
import ir.sharif.mobile.simple_task_management.ui.utils.RecyclerItemTouchHelper;

public class DailiesFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private DailyViewAdapter mAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dailies, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new DailyViewAdapter(getContext(), view);
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
        mAdapter.clearList();
        Executor.getInstance().setHandler(new DailyViewHandler(this));
        Executor.getInstance().loadTasks(TaskRepository.TaskType.DAILY);
        Executor.getInstance().loadScore();
        getActivity().findViewById(R.id.new_button).setOnClickListener(v -> {
            Navigation.findNavController(getActivity().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editDailyFragment);
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof DailyViewAdapter.DailyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = mAdapter.getItem(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final int deletedIndex = viewHolder.getAdapterPosition();
            final Daily deletedTask = mAdapter.getItem(deletedIndex);

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(getView(), name + " removed from Dailies!", Snackbar.LENGTH_LONG);
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

    public DailyViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setDailies(List<Daily> dailies) {
        Date now = new Date();
        for (Daily daily : dailies) {
            long start = daily.getStart().getTime();
            long diff = now.getTime() - start;
            long diffDays = diff / (1000 * 60 * 60 * 24);
            Log.d("Daily_Diff:", Long.toString(diff));
            int every = daily.getEvery();
            if (diffDays >= every) {
//                boolean checked = daily.getLastCheckedDate() != null && daily.getLastCheckedDate().getTime() > daily.getStart().getTime();
                boolean checked = daily.isChecked();
                long passedPeriods = diffDays / every;
                daily.setStart( new Date( daily.getStart().getTime() + passedPeriods * every * (1000 * 60 * 60 * 24) ) );
                if (!checked) {
                    // we can sub reward times passedPeriods but we don't want a user to lose all of their money because they don't check the app
                    Executor.getInstance().subCoin(daily.getReward());
                }
                // set checklist and item to unchecked
                for (ChecklistItem checklistItem : daily.getChecklistItems()) {
                    checklistItem.setChecked(false);
                }
                daily.setChecked(false);
                Executor.getInstance().saveTask(daily);
            }
        }
        mAdapter.addAll((dailies));
    }
}