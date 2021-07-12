package ir.sharif.mobile.project.ui.habits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import ir.sharif.mobile.project.R;

public class HabitsFragment extends Fragment {

    private HabitsViewModel habitsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        habitsViewModel =
                new ViewModelProvider(this).get(HabitsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_habits, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        habitsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}