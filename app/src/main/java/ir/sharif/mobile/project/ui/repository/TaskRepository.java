package ir.sharif.mobile.project.ui.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Daily;
import ir.sharif.mobile.project.ui.model.Habit;
import ir.sharif.mobile.project.ui.model.Reminder;
import ir.sharif.mobile.project.ui.model.Task;
import ir.sharif.mobile.project.ui.model.Todo;

/**
 * This class is a repository class for Daily, Habit and Todo classes.
 */
public class TaskRepository implements BaseRepository<Task> {

    private static final String TABLE_NAME = "task";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + " title VARCHAR(150) NOT NULL, " +
            " description text, reward INTEGER, every INTEGER, start DATETIME(3), dudate DATETIME(3)," +
            " type VARCHAR(20) not null, last_check DATETIME(3));";

    public static final String DELETE_TASK_PATTERN = "DELETE FROM " + TABLE_NAME + " " +
            "WHERE id = %d;";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private ChecklistItemRepository checklistItemRepository;
    private ReminderRepository reminderRepository;
    private final DbHelper dbHelper;

    public TaskRepository(@Nullable DbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE_QUERY);
        this.dbHelper = dbHelper;
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
        if (object.getTitle() != null)
            values.put("title", object.getTitle());
        if (object.getDescription() != null)
            values.put("description", object.getDescription());
        values.put("reward", object.getReward());
        if (object instanceof Todo) {
            if (((Todo) object).getDueDate() != null)
                values.put("dudate", ((Todo) object).getDueDate().toString());
            values.put("type", TaskType.TODO.name());
        } else if (object instanceof Daily) {
            if (((Daily) object).getEvery() != null)
                values.put("every", ((Daily) object).getEvery());
            if (((Daily) object).getStart() != null)
                values.put("start", ((Daily) object).getStart().toString());
            if (((Daily) object).getLastCheckedDate() != null) {
                values.put("last_check", ((Daily) object).getLastCheckedDate().toString());
            }
            values.put("type", TaskType.DAILY.name());
        } else {
            values.put("type", TaskType.HABIT.name());
        }
        long insert = dbHelper.getWritableDatabase().replaceOrThrow(TABLE_NAME, null, values);
        if (object instanceof Todo) {
            for (ChecklistItem checklistItem : ((Todo) object).getChecklistItems()) {
                checklistItem.setTaskId(insert);
                checklistItemRepository.save(checklistItem);
            }
            for (Reminder reminder : ((Todo) object).getReminders()) {
                reminder.setTaskId(insert);
                reminder.setTaskName(object.getTitle());
                reminderRepository.save(reminder);
            }
        } else if (object instanceof Daily) {
            for (ChecklistItem checklistItem : ((Daily) object).getChecklistItems()) {
                checklistItem.setTaskId(insert);
                checklistItemRepository.save(checklistItem);
            }
            for (Reminder reminder : ((Daily) object).getReminders()) {
                reminder.setTaskId(insert);
                reminder.setTaskName(object.getTitle());
                reminderRepository.save(reminder);
            }
        }
        return object.setId(insert);
    }

    @Override
    public List<Task> findAll() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return parseResults(cursor);
    }

    @Override
    public void delete(long id) {
        String query = String.format(DELETE_TASK_PATTERN, id);
        dbHelper.getWritableDatabase().execSQL(query);
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

    public List<Todo> findAllTodo() {
        List<Task> all = findAll(TaskType.TODO);
        List<Todo> todos = new ArrayList<>();
        for (Task task : all) {
            todos.add((Todo) task);
        }
        return todos;
    }

    public List<Daily> findAllDaily() {
        List<Task> all = findAll(TaskType.DAILY);
        List<Daily> todos = new ArrayList<>();
        for (Task task : all) {
            todos.add((Daily) task);
        }
        return todos;
    }

    public List<Habit> findAllHabits() {
        List<Task> all = findAll(TaskType.HABIT);
        List<Habit> todos = new ArrayList<>();
        for (Task task : all) {
            todos.add((Habit) task);
        }
        return todos;
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
        int lastCheckIndex = response.getColumnIndex("last_check");
        while (response.moveToNext()) {
            String type = response.getString(typeIndex);
            Long id = response.getLong(idIndex);
            if (type.equals(TaskType.DAILY.name())) {
                List<ChecklistItem> checkList = checklistItemRepository.findForTask(id);
                List<Reminder> reminders = reminderRepository.findForTask(id);
                Date start = null;
                if (!response.isNull(startIndex))
                    start = new Date(response.getString(startIndex));
                Integer every = null;
                if (!response.isNull(everyIndex)) {
                    every = response.getInt(everyIndex);
                }
                String description = null;
                if (!response.isNull(descriptionIndex)) {
                    description = response.getString(descriptionIndex);
                }
                Date lastCheck = null;
                if (!response.isNull(lastCheckIndex)) {
                    lastCheck = new Date(response.getString(lastCheckIndex));
                }
                items.add(new Daily()
                        .setChecklistItems(checkList)
                        .setReminders(reminders)
                        .setStart(start)
                        .setEvery(every)
                        .setLastCheckedDate(lastCheck)
                        .setId(id)
                        .setTitle(response.getString(titleIndex))
                        .setDescription(description)
                        .setReward(response.getInt(rewardIndex)));

            } else if (type.equals(TaskType.TODO.name())) {
                List<ChecklistItem> checkList = checklistItemRepository.findForTask(id);
                List<Reminder> reminders = reminderRepository.findForTask(id);
                Date dudate = null;
                if (!response.isNull(dudateIndex))
                    dudate = new Date(response.getString(dudateIndex));

                String description = null;
                if (!response.isNull(descriptionIndex)) {
                    description = response.getString(descriptionIndex);
                }
                items.add(new Todo()
                        .setChecklistItems(checkList)
                        .setReminders(reminders)
                        .setDueDate(dudate)
                        .setId(id)
                        .setTitle(response.getString(titleIndex))
                        .setDescription(description)
                        .setReward(response.getInt(rewardIndex)));
            } else {
                String description = null;
                if (!response.isNull(descriptionIndex)) {
                    description = response.getString(descriptionIndex);
                }
                items.add(new Habit()
                        .setId(id)
                        .setTitle(response.getString(titleIndex))
                        .setDescription(description)
                        .setReward(response.getInt(rewardIndex)));
            }
        }
        return items;
    }

    public enum TaskType {
        TODO, DAILY, HABIT;
    }
}
