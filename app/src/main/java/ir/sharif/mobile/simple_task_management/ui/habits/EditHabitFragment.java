package ir.sharif.mobile.simple_task_management.ui.habits;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ir.sharif.mobile.simple_task_management.Executor;
import ir.sharif.mobile.simple_task_management.R;
import ir.sharif.mobile.simple_task_management.model.Habit;
import ir.sharif.mobile.simple_task_management.ui.utils.HideSoftKeyboardHelper;


public class EditHabitFragment extends Fragment {
    private Habit habit;
    private Habit editingHabit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            habit = (Habit) getArguments().getSerializable("habit");
        }
        if (habit == null) {
            habit = Habit.getEmptyHabit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_habit, container, false);

        init_form_values(view);
        init_reward_section(view);

        view.findViewById(R.id.up_button).setOnClickListener(v -> getActivity().onBackPressed());

        view.findViewById(R.id.save_button).setOnClickListener(v -> {
            editingHabit = new Habit();
            editingHabit.setId(habit.getId());
            editingHabit.setTitle(((TextInputEditText) view.findViewById(R.id.input_title)).getText().toString());
            editingHabit.setDescription(((TextInputEditText) view.findViewById(R.id.input_description)).getText().toString());
            editingHabit.setReward(Integer.parseInt(((TextInputEditText) view.findViewById(R.id.input_reward)).getText().toString()));

            if (editingHabit.getTitle().equals("")) {
                Toast.makeText(getContext(), "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            Executor.getInstance().saveTask(editingHabit);
            getActivity().onBackPressed();
        });

        HideSoftKeyboardHelper.setupUI(view, getActivity());
        return view;
    }

    private void init_reward_section(View view) {
        TextInputEditText rewardEditText = view.findViewById(R.id.input_reward);
        rewardEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        view.findViewById(R.id.inc_reward_button).setOnClickListener(v -> {
            String reward = rewardEditText.getText().toString();
            int val = reward.trim().length() == 0 ? 0 : Integer.parseInt(reward);
            rewardEditText.setText(String.valueOf(val + 1));
        });

        view.findViewById(R.id.dec_reward_button).setOnClickListener(v -> {
            String reward = rewardEditText.getText().toString();
            int val = reward.trim().length() == 0 ? 0 : Integer.parseInt(reward);
            rewardEditText.setText(String.valueOf(val - 1));
        });
    }

    void init_form_values(View view) {
        ((TextInputEditText) view.findViewById(R.id.input_title)).setText(habit.getTitle());
        ((TextInputEditText) view.findViewById(R.id.input_description)).setText(habit.getDescription());
        ((TextInputEditText) view.findViewById(R.id.input_reward)).setText(String.valueOf(habit.getReward()));
    }

}
