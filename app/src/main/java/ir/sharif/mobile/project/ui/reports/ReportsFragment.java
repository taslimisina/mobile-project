package ir.sharif.mobile.project.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

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
        TextView seekBarValue = root.findViewById(R.id.seek_bar_value);
        SeekBar scoreSeekBar = root.findViewById(R.id.score_seek_bar);
        scoreSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));

                int width = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
                int thumbPos = seekBar.getPaddingLeft() + width * seekBar.getProgress() / seekBar.getMax();
                int txtW = seekBarValue.getMeasuredWidth();
                int delta = txtW / 2;
                seekBarValue.setX(seekBar.getX() + thumbPos - delta);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                reportsViewModel.setLastDataSize(seekBar.getProgress());
                chart.setData(reportsViewModel.getData().getValue());
                chart.invalidate();
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        getActivity().findViewById(R.id.new_button).setVisibility(View.VISIBLE);
        super.onDestroy();
    }
}