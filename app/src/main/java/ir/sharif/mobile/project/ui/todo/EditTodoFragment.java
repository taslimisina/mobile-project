package ir.sharif.mobile.project.ui.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Reminder;
import ir.sharif.mobile.project.ui.model.Todo;
import ir.sharif.mobile.project.ui.utils.EditChecklistViewAdaptor;
import ir.sharif.mobile.project.ui.utils.EditReminderViewAdaptor;

public class EditTodoFragment extends Fragment {
    private Todo todo;

    private RecyclerView reminderRecyclerview;
    private EditReminderViewAdaptor reminderViewAdaptor;
    private ImageButton addReminderButton;

    private RecyclerView checklistRecyclerview;
    private EditChecklistViewAdaptor checklistViewAdaptor;
    private ImageButton addChecklistItemButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            todo = (Todo)getArguments().getSerializable("todo");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_todo, container, false);

        // Setup reminder section
        reminderRecyclerview = view.findViewById(R.id.reminders_recyclerview);
        RecyclerView.LayoutManager reminderLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        reminderRecyclerview.setLayoutManager(reminderLayoutManager);
        reminderRecyclerview.setItemAnimator(new DefaultItemAnimator());
        reminderRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        reminderRecyclerview.setNestedScrollingEnabled(false);
        reminderViewAdaptor = new EditReminderViewAdaptor(todo.getReminders(), getContext());
        reminderRecyclerview.setAdapter(reminderViewAdaptor);
        // Add reminder
        addReminderButton = view.findViewById(R.id.add_reminder_button);
        addReminderButton.setOnClickListener(v -> {
            Log.v("TODO", "New reminder");
            reminderViewAdaptor.addItem(new Reminder(), todo.getReminders().size());
        });

        // Setup checklist section
        checklistRecyclerview = view.findViewById(R.id.checklist_recyclerview);
        RecyclerView.LayoutManager checklistLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        checklistRecyclerview.setLayoutManager(checklistLayoutManager);
        checklistRecyclerview.setItemAnimator(new DefaultItemAnimator());
        checklistRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        checklistRecyclerview.setNestedScrollingEnabled(false);
        checklistViewAdaptor = new EditChecklistViewAdaptor(todo.getChecklistItems());
        checklistRecyclerview.setAdapter(checklistViewAdaptor);
        // Add checklist item
        addChecklistItemButton = view.findViewById(R.id.add_checklist_item_button);
        addChecklistItemButton.setOnClickListener(v -> {
            Log.v("TODO", "New checklist item");
            checklistViewAdaptor.addItem(new ChecklistItem() , todo.getChecklistItems().size());
        });

        return view;
    }
}
