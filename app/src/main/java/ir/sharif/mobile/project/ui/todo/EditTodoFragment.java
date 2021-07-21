package ir.sharif.mobile.project.ui.todo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.time.Month;
import java.util.Calendar;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Reminder;
import ir.sharif.mobile.project.ui.model.Todo;
import ir.sharif.mobile.project.ui.repository.RepositoryHolder;
import ir.sharif.mobile.project.ui.repository.TaskRepository;
import ir.sharif.mobile.project.ui.utils.EditChecklistViewAdaptor;
import ir.sharif.mobile.project.ui.utils.EditReminderViewAdaptor;

public class EditTodoFragment extends Fragment {
    private Todo todo;
    private Todo editingTodo;
    private RecyclerView reminderRecyclerview;
    private EditReminderViewAdaptor reminderViewAdaptor;
    private RecyclerView checklistRecyclerview;
    private EditChecklistViewAdaptor checklistViewAdaptor;
    private static final TaskRepository taskRepository = RepositoryHolder.getTaskRepository();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            todo = (Todo) getArguments().getSerializable("todo");
        }
        if (todo == null) {
            todo = Todo.getEmptyTodo();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_todo, container, false);

        init_form_values(view);
        init_reminder_section(view);
        init_checklist_section(view);
        init_reward_section(view);
        init_due_date_section(view);

        view.findViewById(R.id.up_button).setOnClickListener(v -> {
            // TODO: 7/20/21 discard changes
            getActivity().onBackPressed();
        });

        view.findViewById(R.id.save_button).setOnClickListener(v -> {
            // TODO: 7/20/21 save changes or create new todo

            getActivity().onBackPressed();
        });

        return view;
    }

    private void init_due_date_section(View view) {
        TextInputEditText dueDateEditText = view.findViewById(R.id.input_due_date);
        Button clearDueDateButton = view.findViewById(R.id.clear_due_date);

        clearDueDateButton.setOnClickListener(v -> {
            dueDateEditText.setText("");
        });

        dueDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    clearDueDateButton.setVisibility(View.INVISIBLE);
                } else {
                    clearDueDateButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dueDateEditText.setOnClickListener(v -> {
            String dueDate = dueDateEditText.getText().toString().trim();
            int dd, mm, yyyy;
            if (dueDate.length() == 0) {
                final Calendar currentDate = Calendar.getInstance();
                dd = currentDate.get(Calendar.DAY_OF_MONTH);
                mm = currentDate.get(Calendar.MONTH);
                yyyy = currentDate.get(Calendar.YEAR);
            } else {
                String[] dateParts = dueDate.split("-");
                dd = Integer.parseInt(dateParts[0]);
                mm = Integer.parseInt(dateParts[1]);
                yyyy = Integer.parseInt(dateParts[2]);
            }
            DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dueDateEditText.setText(getContext().getString(R.string.date, dayOfMonth, monthOfYear, year));
                }
            }, yyyy, mm, dd);
            datePicker.show();

        });
    }

    private void init_reward_section(View view) {
        TextInputEditText rewardEditText = view.findViewById(R.id.input_reward);
        rewardEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        view.findViewById(R.id.inc_reward_button).setOnClickListener(v -> {
            String reward = rewardEditText.getText().toString();
            int val = reward.trim().length() == 0 ? 0 : Integer.parseInt(reward);
            rewardEditText.setText(String.valueOf(val + 1));
        });

        view.findViewById(R.id.dec_reward_button).setOnClickListener(v -> {
            String reward = rewardEditText.getText().toString();
            int val = reward.trim().length() == 0 ? 0 : Integer.parseInt(reward);
            rewardEditText.setText(String.valueOf(val > 0 ? val - 1 : 0));
        });
    }

    private void init_checklist_section(View view) {
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
        ImageButton addChecklistItemButton = view.findViewById(R.id.add_checklist_item_button);
        addChecklistItemButton.setOnClickListener(v -> {
            checklistViewAdaptor.addItem(new ChecklistItem(), todo.getChecklistItems().size());
        });
    }

    private void init_reminder_section(View view) {
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
        reminderViewAdaptor = new EditReminderViewAdaptor(todo.getReminders(), getContext());
        reminderRecyclerview.setAdapter(reminderViewAdaptor);
        // Add reminder
        ImageButton addReminderButton = view.findViewById(R.id.add_reminder_button);
        addReminderButton.setOnClickListener(v -> {
            reminderViewAdaptor.addItem(new Reminder().setTime(Calendar.getInstance().getTime()), todo.getReminders().size());
        });
    }

    void init_form_values(View view) {
        ((TextInputEditText) view.findViewById(R.id.input_title)).setText(todo.getTitle());
        ((TextInputEditText) view.findViewById(R.id.input_description)).setText(todo.getDescription());
        ((TextInputEditText) view.findViewById(R.id.input_reward)).setText(String.valueOf(todo.getReward()));
        if (todo.getDueDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(todo.getDueDate());
            String duDate = getContext().getString(R.string.date, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            ((TextInputEditText) view.findViewById(R.id.input_due_date)).setText(duDate);
        }
    }
}
