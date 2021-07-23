package ir.sharif.mobile.simple_task_management.ui.reports;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ir.sharif.mobile.simple_task_management.repository.RepositoryHolder;

public class ReportsViewModel extends ViewModel {

    private MutableLiveData<LineData> data;
    private int lastDataSize = 30;

    public ReportsViewModel() {
        data = new MutableLiveData<>();
    }

    public ReportsViewModel setLastDataSize(int lastDataSize) {
        this.lastDataSize = lastDataSize;
        return this;
    }

    public LiveData<LineData> getData() {
        Map<Date, Integer> history = RepositoryHolder.getCoinRepository().getHistory();
        List<Date> dates = new ArrayList<>(history.keySet());

        Collections.sort(dates, (o1, o2) -> o1.after(o2) ? -1 : o1.equals(o2) ? 0 : 1);
        List<String> labels = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();
        int dataSize = Math.min(lastDataSize, dates.size()-1);
        for (int i = 0; i <= dataSize; i++) {
            Date date = dates.get(i);
            entries.add(new Entry(history.get(date), dataSize - i));
            labels.add(date.getMonth() + "/" + date.getDay() + " " + date.getHours() + ":" + date.getMinutes());
        }
        data.setValue(new LineData(labels, new LineDataSet(entries, "score")));
        return data;
    }
}