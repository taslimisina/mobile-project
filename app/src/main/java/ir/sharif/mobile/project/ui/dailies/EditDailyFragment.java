package ir.sharif.mobile.project.ui.dailies;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Daily;

public class EditDailyFragment extends Fragment {
    private Daily daily;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            daily = (Daily)getArguments().getSerializable("daily");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_daily, container, false);
    }
}