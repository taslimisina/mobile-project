package ir.sharif.mobile.project.ui.habits;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import ir.sharif.mobile.project.Executor;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.model.Habit;


public class HabitViewHandler extends Handler {

    public static final int LOAD_DONE = 0;
    public static final int UPDATE_SCORE = Executor.UPDATE_SCORE;
    public static final int SHOW_TOAST = Executor.SHOW_TOAST;

    private WeakReference<HabitFragment> habitFragment;

    public HabitViewHandler(HabitFragment habitFragment) {
        this.habitFragment = new WeakReference<>(habitFragment);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (habitFragment == null)
            return;

        switch (msg.what) {
            case LOAD_DONE:
                habitFragment.get().getAdapter().addAll(((List<Habit>) msg.obj));
                break;
            case SHOW_TOAST:
                String text = (String) msg.obj;
                Toast.makeText(habitFragment.get().getContext(), text, Toast.LENGTH_LONG)
                        .show();
                break;
            case UPDATE_SCORE:
                TextView view = ((TextView) habitFragment.get().getActivity().findViewById(R.id.score));
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
