package ir.sharif.mobile.project.ui.habits;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import ir.sharif.mobile.project.ui.model.Task;
import ir.sharif.mobile.project.ui.repository.RepositoryHolder;
import ir.sharif.mobile.project.ui.repository.TaskRepository;


public class TaskViewHandler extends Handler {

    public static final int DELETE_DATA = 0;


    public TaskViewHandler() {
        super(Looper.myLooper());
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        TaskRepository helper = RepositoryHolder.getTaskRepository();
        if (helper == null) {
            return;
        }

        switch (msg.what) {
            case DELETE_DATA:
                helper.delete(((Task) msg.obj).getId());
                break;
            default:
                Log.e("Handler", "Unknown Message");
        }
    }
}
