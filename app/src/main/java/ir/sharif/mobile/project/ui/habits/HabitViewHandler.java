package ir.sharif.mobile.project.ui.habits;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import ir.sharif.mobile.project.ui.model.Habit;
import ir.sharif.mobile.project.ui.repository.RepositoryHolder;
import ir.sharif.mobile.project.ui.repository.TaskRepository;


public class HabitViewHandler extends Handler {

    public static final int DELETE_TASK = 0;
    public static final int DONE_TASK = 1;
    public static final int SAVE_TASK = 2;


    public HabitViewHandler() {
        super(Looper.myLooper());
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        TaskRepository helper = RepositoryHolder.getTaskRepository();
        if (helper == null) {
            return;
        }

        switch (msg.what) {
            case DELETE_TASK:
                helper.delete(((Habit) msg.obj).getId());
                break;
            case DONE_TASK:
                // TODO: 7/21/21 add reward
                break;
            case SAVE_TASK:
                helper.save((Habit) msg.obj);
                break;
            default:
                Log.e("Handler", "Unknown Message");
        }
    }
}
