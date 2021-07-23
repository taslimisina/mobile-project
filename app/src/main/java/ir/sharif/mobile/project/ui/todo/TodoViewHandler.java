package ir.sharif.mobile.project.ui.todo;

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
import ir.sharif.mobile.project.model.Todo;


public class TodoViewHandler extends Handler {

    public static final int LOAD_DONE = 0;
    public static final int UPDATE_SCORE = Executor.UPDATE_SCORE;
    public static final int SHOW_TOAST = Executor.SHOW_TOAST;

    private WeakReference<TodoFragment> todoFragment;

    public TodoViewHandler(TodoFragment todoFragment) {
        this.todoFragment = new WeakReference<>(todoFragment);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (todoFragment == null)
            return;

        switch (msg.what) {
            case LOAD_DONE:
                todoFragment.get().getAdapter().addAll(((List<Todo>) msg.obj));
                break;
            case SHOW_TOAST:
                String text = (String) msg.obj;
                Toast.makeText(todoFragment.get().getContext(), text, Toast.LENGTH_LONG)
                        .show();
                break;
            case UPDATE_SCORE:
                TextView view = ((TextView) todoFragment.get().getActivity().findViewById(R.id.score));
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
