package ir.sharif.mobile.project.ui.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Daily;
import ir.sharif.mobile.project.ui.model.Habit;
import ir.sharif.mobile.project.ui.model.Reminder;
import ir.sharif.mobile.project.ui.model.Reward;
import ir.sharif.mobile.project.ui.model.Task;
import ir.sharif.mobile.project.ui.model.Todo;

/**
 * This class is a repository class for Daily, Habit and Todo classes.
 */
public class TaskRepository extends SQLiteOpenHelper implements BaseRepository<Task> {

    private static final String TABLE_NAME = "task";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "title VARCHAR(150) NOT NULL, " +
            " description text, reward INTEGER, every INTEGER, start DATETIME(3), dudate DATETIME(3)," +
            " type VARCHAR(20) not null);";

    public static final String DELETE_TASK_PATTERN = "DELETE FROM " + TABLE_NAME + " " +
            "WHERE id = %d;";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private ChecklistItemRepository checklistItemRepository;
    private ReminderRepository reminderRepository;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public TaskRepository(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public TaskRepository setChecklistItemRepository(ChecklistItemRepository checklistItemRepository) {
        this.checklistItemRepository = checklistItemRepository;
        return this;
    }

    public TaskRepository setReminderRepository(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
        return this;
    }

    @Override
    public Task save(Task object) {
        ContentValues values = new ContentValues();
        if (object.getId() != null) {
            values.put("id", object.getId());
        }
        values.put("title", object.getTitle());
        values.put("description", object.getDescription());
        values.put("reward", object.getReward());
        if (object instanceof Todo) {
            values.put("dudate", ((Todo) object).getDueDate().toString());
            values.put("type", TaskType.TODO.name());
            for (ChecklistItem checklistItem : ((Todo) object).getChecklistItems()) {
                checklistItemRepository.save(checklistItem);
            }
            for (Reminder reminder : ((Todo) object).getReminders()) {
                reminderRepository.save(reminder);
            }
        } else if (object instanceof Daily) {
            values.put("every", ((Daily) object).getEvery());
            values.put("start", ((Daily) object).getStart().toString());
            values.put("type", TaskType.DAILY.name());
            for (ChecklistItem checklistItem : ((Todo) object).getChecklistItems()) {
                checklistItemRepository.save(checklistItem);
            }
            for (Reminder reminder : ((Todo) object).getReminders()) {
                reminderRepository.save(reminder);
            }
        } else {
            values.put("type", TaskType.HABIT.name());
        }
        long insert = getWritableDatabase().replaceOrThrow(TABLE_NAME, null, values);
        return object.setId(insert);
    }

    @Override
    public List<Task> findAll() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return parseResults(cursor);
    }

    @Override
    public void delete(long id) {
        String query = String.format(DELETE_TASK_PATTERN, id);
        getWritableDatabase().execSQL(query);
    }

    public List<Task> findAll(TaskType taskType) {
        List<Task> all = findAll();
        Iterator<Task> iterator = all.iterator();
        while (iterator.hasNext()) {
            switch (taskType) {
                case TODO:
                    if (!(iterator.next() instanceof Todo)) {
                        iterator.remove();
                    }
                    break;
                case DAILY:
                    if (!(iterator.next() instanceof Daily)) {
                        iterator.remove();
                    }
                    break;
                case HABIT:
                    if (!(iterator.next() instanceof Habit)) {
                        iterator.remove();
                    }
                    break;
            }
        }
        return all;
    }

    private List<Task> parseResults(Cursor response) {
        List<Task> items = new ArrayList<>();
        int idIndex = response.getColumnIndex("id");
        int titleIndex = response.getColumnIndex("title");
        int descriptionIndex = response.getColumnIndex("description");
        int rewardIndex = response.getColumnIndex("reward");
        int everyIndex = response.getColumnIndex("every");
        int startIndex = response.getColumnIndex("start");
        int dudateIndex = response.getColumnIndex("dudate");
        int typeIndex = response.getColumnIndex("type");
        while (response.moveToNext()) {
            String type = response.getString(typeIndex);
            Long id = response.getLong(idIndex);
            if (type.equals(TaskType.DAILY.name())) {
                List<ChecklistItem> checkList = checklistItemRepository.findForTask(id);
                List<Reminder> reminders = reminderRepository.findForTask(id);
                items.add(new Daily()
                        .setChecklistItems(checkList)
                        .setReminders(reminders)
                        .setStart(new Date(response.getString(startIndex)))
                        .setEvery(response.getInt(everyIndex))
                        .setId(id)
                        .setTitle(response.getString(titleIndex))
                        .setDescription(response.getString(descriptionIndex))
                        .setReward(response.getInt(rewardIndex)));

            } else if (type.equals(TaskType.TODO.name())) {
                List<ChecklistItem> checkList = checklistItemRepository.findForTask(id);
                List<Reminder> reminders = reminderRepository.findForTask(id);
                items.add(new Todo()
                        .setChecklistItems(checkList)
                        .setReminders(reminders)
                        .setDueDate(new Date(response.getString(dudateIndex)))
                        .setId(id)
                        .setTitle(response.getString(titleIndex))
                        .setDescription(response.getString(descriptionIndex))
                        .setReward(response.getInt(rewardIndex)));
            } else {
                items.add(new Habit()
                        .setId(id)
                        .setTitle(response.getString(titleIndex))
                        .setDescription(response.getString(descriptionIndex))
                        .setReward(response.getInt(rewardIndex)));
            }
        }
        return items;
    }

    public enum TaskType {
        TODO, DAILY, HABIT;
    }
}
