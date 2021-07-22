package ir.sharif.mobile.project.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class ReportsFragment extends Fragment {

    private ReportsViewModel reportsViewModel;
    private LineChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                new ViewModelProvider(this).get(ReportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        chart = root.findViewById(R.id.line_chart);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        chart.getXAxis().setTextSize(50f);
        chart.getAxisLeft().setTextSize(50f);
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        Map<Date, Integer> history = RepositoryHolder.getCoinRepository().getHistory();
        List<Date> dates = new ArrayList<>(history.keySet());

        Collections.sort(dates, (o1, o2) -> o1.after(o2) ? 1 : o1.equals(o2) ? 0 : -1);
        List<String> labels = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < Math.min(20, dates.size()); i++) {
            Date date = dates.get(i);
            entries.add(new Entry(history.get(date), i));
            labels.add(date.getMonth() + "/" + date.getDay() + " " + date.getHours() + ":" + date.getMinutes());
        }
        chart.setData(new LineData(labels, new LineDataSet(entries, "score")));
        chart.notifyDataSetChanged();
        return root;
    }
}