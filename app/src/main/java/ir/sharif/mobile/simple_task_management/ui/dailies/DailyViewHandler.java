package ir.sharif.mobile.simple_task_management.ui.dailies;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import ir.sharif.mobile.simple_task_management.Executor;
import ir.sharif.mobile.simple_task_management.R;
import ir.sharif.mobile.simple_task_management.model.Daily;

public class DailyViewHandler extends Handler {
    public static final int LOAD_DONE = 0;
    public static final int UPDATE_SCORE = Executor.UPDATE_SCORE;
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
                dailyFragment.get().setDailies((List<Daily>) msg.obj);
                break;
            case SHOW_TOAST:
                String text = (String) msg.obj;
                Toast.makeText(dailyFragment.get().getContext(), text, Toast.LENGTH_LONG)
                        .show();
                break;
            case UPDATE_SCORE:
                TextView view = ((TextView) dailyFragment.get().getActivity().findViewById(R.id.score));
                int newScore = (int) msg.obj;
                int oldScore = Integer.parseInt(view.getText().toString());
                ValueAnimator valueAnimator = ValueAnimator.ofInt(oldScore, newScore);
                valueAnimator.setDuration(800);
                valueAnimator.addUpdateListener(valueAnimator1 -> view.setText(valueAnimator1.getAnimatedValue().toString()));
                valueAnimator.start();
                break;
            default:
                Log.e("Handler", "Unknown Message");
        }
    }
}
