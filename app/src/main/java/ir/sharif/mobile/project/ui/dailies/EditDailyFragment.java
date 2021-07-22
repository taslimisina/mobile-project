package ir.sharif.mobile.project.ui.dailies;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.sharif.mobile.project.Executor;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Daily;
import ir.sharif.mobile.project.ui.model.Reminder;
import ir.sharif.mobile.project.ui.model.utils.DateUtil;
import ir.sharif.mobile.project.ui.utils.EditChecklistViewAdapter;
import ir.sharif.mobile.project.ui.utils.EditReminderViewAdapter;
import ir.sharif.mobile.project.ui.utils.HideSoftKeyboardHelper;

public class EditDailyFragment extends Fragment {
    private Daily daily;
    private Daily editingDaily;
    private RecyclerView reminderRecyclerview;
    private EditReminderViewAdapter reminderViewAdapter;
    private RecyclerView checklistRecyclerview;
    private EditChecklistViewAdapter checklistViewAdapter;
    private DailyViewHandler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            daily = (Daily) getArguments().getSerializable("daily");
        }
        if (daily == null) {
            daily = Daily.getEmptyDaily();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_daily, container, false);

        handler = new DailyViewHandler(null);

        init_form_values(view);
        init_reminder_section(view);
        init_checklist_section(view);
        init_reward_section(view);
        init_start_date_section(view);

        view.findViewById(R.id.up_button).setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        view.findViewById(R.id.save_button).setOnClickListener(v -> {
            editingDaily = new Daily();
            editingDaily.setId(daily.getId());
            editingDaily.setTitle(((TextInputEditText) view.findViewById(R.id.input_title)).getText().toString());
            editingDaily.setDescription(((TextInputEditText) view.findViewById(R.id.input_description)).getText().toString());
            editingDaily.setReward(Integer.parseInt(((TextInputEditText) view.findViewById(R.id.input_reward)).getText().toString()));
            editingDaily.setChecklistItems(daily.getChecklistItems());
            editingDaily.setStart(daily.getStart());
            editingDaily.setEvery(Integer.parseInt(((TextInputEditText) view.findViewById(R.id.input_repeat)).getText().toString()));
            editingDaily.setReminders(daily.getReminders());
            if (editingDaily.getTitle().equals("")) {
                Toast.makeText(getContext(), "Title shouldn't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (long id : checklistViewAdapter.getToBeDeletedItems())
                Executor.getInstance().deleteChecklistItem(id);
            for (long id : reminderViewAdapter.getToBeDeletedReminders())
                Executor.getInstance().deleteReminder(id);

            Executor.getInstance().saveTask(editingDaily);
            getActivity().onBackPressed();
        });

        HideSoftKeyboardHelper.setupUI(view, getActivity());
        return view;
    }

    private void init_start_date_section(View view) {
        TextInputEditText startDateEditText = view.findViewById(R.id.input_start_date);
        Button clearStartDateButton = view.findViewById(R.id.clear_start_date);

        clearStartDateButton.setOnClickListener(v -> {
            startDateEditText.setText("");
            daily.setStart(null);
        });

        if (startDateEditText.getText().toString().length() == 0) {
            clearStartDateButton.setVisibility(View.INVISIBLE);
        }
        else {
            clearStartDateButton.setVisibility(View.VISIBLE);
        }
        startDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    clearStartDateButton.setVisibility(View.INVISIBLE);
                } else {
                    clearStartDateButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        startDateEditText.setOnClickListener(v -> {
            String startDate = startDateEditText.getText().toString().trim();
            final Calendar calendar = Calendar.getInstance();
            if (startDate.length() != 0)
                calendar.setTime(DateUtil.parseDate(startDate));
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            int mm = calendar.get(Calendar.MONTH);
            int yyyy = calendar.get(Calendar.YEAR);

            DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    startDateEditText.setText(DateUtil.formatDate(year, monthOfYear+1, dayOfMonth, DateUtil.LONG));
                    Calendar cal = new GregorianCalendar();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    daily.setStart(cal.getTime());    //todo
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
        checklistViewAdapter = new EditChecklistViewAdapter(daily.getChecklistItems());
        checklistRecyclerview.setAdapter(checklistViewAdapter);
        // Add checklist item
        ImageButton addChecklistItemButton = view.findViewById(R.id.add_checklist_item_button);
        addChecklistItemButton.setOnClickListener(v -> {
            checklistViewAdapter.addItem(new ChecklistItem(), daily.getChecklistItems().size());
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
        reminderViewAdapter = new EditReminderViewAdapter(daily.getReminders(), getContext());
        reminderRecyclerview.setAdapter(reminderViewAdapter);
        // Add reminder
        ImageButton addReminderButton = view.findViewById(R.id.add_reminder_button);
        addReminderButton.setOnClickListener(v -> {
            reminderViewAdapter.addItem(new Reminder().setTime(Calendar.getInstance().getTime()), daily.getReminders().size());
        });
    }

    void init_form_values(View view) {
        ((TextInputEditText) view.findViewById(R.id.input_title)).setText(daily.getTitle());
        ((TextInputEditText) view.findViewById(R.id.input_description)).setText(daily.getDescription());
        ((TextInputEditText) view.findViewById(R.id.input_reward)).setText(String.valueOf(daily.getReward()));
        if (daily.getStart() != null) {   //todo
            String stDate = DateUtil.formatDate(daily.getStart(), DateUtil.LONG);
            ((TextInputEditText) view.findViewById(R.id.input_start_date)).setText(stDate);
        }
        ((TextInputEditText) view.findViewById(R.id.input_repeat)).setText(String.valueOf(daily.getEvery()));
    }

    @Override
    public void onStart() {
        super.onStart();
        checklistViewAdapter.clearToBeDeletedItems();
        reminderViewAdapter.clearToBeDeletedReminders();
    }
}