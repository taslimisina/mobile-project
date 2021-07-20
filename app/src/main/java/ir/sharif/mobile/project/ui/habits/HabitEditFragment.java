package ir.sharif.mobile.project.ui.habits;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Task;

public class HabitEditFragment extends Fragment {
    private Task task;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task = (Task)getArguments().getSerializable("task");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_habit, container, false);
//        view.findViewById(R.id.upButton).setOnClickListener();    // Todo set on click listener (navigate back)
        return view;
    }
}