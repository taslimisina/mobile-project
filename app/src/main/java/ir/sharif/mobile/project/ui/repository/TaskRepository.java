package ir.sharif.mobile.project.ui.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import ir.sharif.mobile.project.ui.model.Task;

/**
 * This class is a repository class for Daily, Habit and Todo classes.
 */
public class TaskRepository extends SQLiteOpenHelper implements BaseRepository<Task> {

    public static final String TABLE_NAME = "TASK";

    public TaskRepository(@Nullable Context context, ReminderRepository reminderRepository,
                          ChecklistItemRepository checklistItemRepository) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public Task save(Task object) {
        return null;
    }

    @Override
    public List<Task> findAll() {
        return Collections.emptyList();
    }

    public List<Task> findAll(TaskType taskType) {
        return Collections.emptyList();
    }

    @Override
    public Task findById(Long id) {
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public enum TaskType {
        TODO, DAILY, REWARDS;
    }
}
