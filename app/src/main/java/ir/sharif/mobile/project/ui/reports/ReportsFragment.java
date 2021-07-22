package ir.sharif.mobile.project.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.repository.RepositoryHolder;

import static android.view.View.INVISIBLE;

public class ReportsFragment extends Fragment {

    private ReportsViewModel reportsViewModel;
    private LineChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                new ViewModelProvider(this).get(ReportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        chart = root.findViewById(R.id.line_chart);
        chart.setDescriptionTextSize(25f);
        chart.setDescription("Score over time");
        chart.getLegend().setTextSize(25f);
        reportsViewModel.getData().observe(getViewLifecycleOwner(), new Observer<LineData>() {
            @Override
            public void onChanged(LineData lineData) {
                chart.setData(lineData);
                chart.invalidate();
            }
        });
        getActivity().findViewById(R.id.new_button).setVisibility(INVISIBLE);
        return root;
    }

    @Override
    public void onDestroy() {
        getActivity().findViewById(R.id.new_button).setVisibility(View.VISIBLE);
        super.onDestroy();
    }
}