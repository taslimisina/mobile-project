package ir.sharif.mobile.simple_task_management.ui.todo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.sharif.mobile.simple_task_management.Executor;
import ir.sharif.mobile.simple_task_management.R;
import ir.sharif.mobile.simple_task_management.model.ChecklistItem;
import ir.sharif.mobile.simple_task_management.model.Reminder;
import ir.sharif.mobile.simple_task_management.model.Todo;
import ir.sharif.mobile.simple_task_management.util.DateUtil;
import ir.sharif.mobile.simple_task_management.ui.utils.EditChecklistViewAdapter;
import ir.sharif.mobile.simple_task_management.ui.utils.EditReminderViewAdapter;
import ir.sharif.mobile.simple_task_management.ui.utils.HideSoftKeyboardHelper;


public class EditTodoFragment extends Fragment {
    private Todo todo;
    private Todo editingTodo;
    private RecyclerView reminderRecyclerview;
    private EditReminderViewAdapter reminderViewAdapter;
    private RecyclerView checklistRecyclerview;
    private EditChecklistViewAdapter checklistViewAdapter;
    private TodoViewHandler handler;

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

        handler = new TodoViewHandler(null);

        init_form_values(view);
        init_reminder_section(view);
        init_checklist_section(view);
        init_reward_section(view);
        init_due_date_section(view);

        view.findViewById(R.id.up_button).setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        view.findViewById(R.id.save_button).setOnClickListener(v -> {
            editingTodo = new Todo();
            editingTodo.setId(todo.getId());
            editingTodo.setTitle(((TextInputEditText) view.findViewById(R.id.input_title)).getText().toString());
            editingTodo.setDescription(((TextInputEditText) view.findViewById(R.id.input_description)).getText().toString());
            editingTodo.setReward(Integer.parseInt(((TextInputEditText) view.findViewById(R.id.input_reward)).getText().toString()));
            editingTodo.setChecklistItems(todo.getChecklistItems());
            editingTodo.setDueDate(todo.getDueDate());
            editingTodo.setReminders(todo.getReminders());
            if (editingTodo.getTitle().equals("")) {
                Toast.makeText(getContext(), "Title shouldn't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (long id : checklistViewAdapter.getToBeDeletedItems())
                Executor.getInstance().deleteChecklistItem(id);
            for (long id : reminderViewAdapter.getToBeDeletedReminders())
                Executor.getInstance().deleteReminder(id);

            Executor.getInstance().saveTask(editingTodo);
            getActivity().onBackPressed();
        });

        HideSoftKeyboardHelper.setupUI(view, getActivity());
        return view;
    }

    private void init_due_date_section(View view) {
        TextInputEditText dueDateEditText = view.findViewById(R.id.input_due_date);
        Button clearDueDateButton = view.findViewById(R.id.clear_due_date);

        clearDueDateButton.setOnClickListener(v -> {
            dueDateEditText.setText("");
            todo.setDueDate(null);
        });

        if (dueDateEditText.getText().toString().length() == 0) {
            clearDueDateButton.setVisibility(View.INVISIBLE);
        }
        else {
            clearDueDateButton.setVisibility(View.VISIBLE);
        }
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
            final Calendar calendar = Calendar.getInstance();
            if (dueDate.length() != 0)
                calendar.setTime(DateUtil.parseDate(dueDate));
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            int mm = calendar.get(Calendar.MONTH);
            int yyyy = calendar.get(Calendar.YEAR);

            DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    dueDateEditText.setText(DateUtil.formatDate(year, monthOfYear+1, dayOfMonth, DateUtil.LONG));
                    Calendar cal = new GregorianCalendar();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    todo.setDueDate(cal.getTime());
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
        checklistViewAdapter = new EditChecklistViewAdapter(todo.getChecklistItems());
        checklistRecyclerview.setAdapter(checklistViewAdapter);
        // Add checklist item
        ImageButton addChecklistItemButton = view.findViewById(R.id.add_checklist_item_button);
        addChecklistItemButton.setOnClickListener(v -> {
            checklistViewAdapter.addItem(new ChecklistItem(), todo.getChecklistItems().size());
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
        reminderViewAdapter = new EditReminderViewAdapter(todo.getReminders(), getContext());
        reminderRecyclerview.setAdapter(reminderViewAdapter);
        // Add reminder
        ImageButton addReminderButton = view.findViewById(R.id.add_reminder_button);
        addReminderButton.setOnClickListener(v -> {
            reminderViewAdapter.addItem(new Reminder().setTime(Calendar.getInstance().getTime()), todo.getReminders().size());
        });
    }

    void init_form_values(View view) {
        ((TextInputEditText) view.findViewById(R.id.input_title)).setText(todo.getTitle());
        ((TextInputEditText) view.findViewById(R.id.input_description)).setText(todo.getDescription());
        ((TextInputEditText) view.findViewById(R.id.input_reward)).setText(String.valueOf(todo.getReward()));
        if (todo.getDueDate() != null) {
            String duDate = DateUtil.formatDate(todo.getDueDate(), DateUtil.LONG);
            ((TextInputEditText) view.findViewById(R.id.input_due_date)).setText(duDate);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checklistViewAdapter.clearToBeDeletedItems();
        reminderViewAdapter.clearToBeDeletedReminders();
    }
}
