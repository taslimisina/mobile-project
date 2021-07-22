package ir.sharif.mobile.project.ui.dailies;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import ir.sharif.mobile.project.Executor;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Daily;

public class DailyViewHandler extends Handler {
    public static final int LOAD_DONE = 0;
    public static final int UPDATE_SCORE = 2;
    public static final int SHOW_TOAST = Executor.SHOW_TOAST;

    private WeakReference<DailiesFragment> dailyFragment;

    public DailyViewHandler(DailiesFragment dailyFragment) {
        this.dailyFragment = new WeakReference<>(dailyFragment);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (dailyFragment == null)
            return;

        switch (msg.what) {
            case LOAD_DONE:
                dailyFragment.get().getAdapter().addAll(((List<Daily>) msg.obj));
                break;
            case SHOW_TOAST:
                String text = (String) msg.obj;
                Toast.makeText(dailyFragment.get().getContext(), text, Toast.LENGTH_LONG)
                        .show();
                break;
            case UPDATE_SCORE:
                String score = String.valueOf((int) msg.obj);
                ((TextView) dailyFragment.get().getActivity().findViewById(R.id.score)).setText(score);
                break;
            default:
                Log.e("Handler", "Unknown Message");
        }
    }
}
