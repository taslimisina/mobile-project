package ir.sharif.mobile.project.ui.habits;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;


public class TaskViewHandler extends Handler {

    public static final int DELETE_DATA = 0;

    private final WeakReference<TaskDbHelper> helper;

    public TaskViewHandler(TaskDbHelper db) {
        super(Looper.myLooper());
        this.helper = new WeakReference<>(db);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        TaskDbHelper helper = this.helper.get();
        if (helper == null) {
            return;
        }

        switch (msg.what) {
            case DELETE_DATA:
                helper.delete((Task) msg.obj);
                break;
            default:
                Log.e("Handler", "Unknown Message");
        }
    }
}
