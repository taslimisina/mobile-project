package ir.sharif.mobile.project.ui.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ir.sharif.mobile.project.ui.model.Reminder;

public class ReminderRepository implements BaseRepository<Reminder> {

    private static final String TABLE_NAME = "remider";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "time datetime(3) NOT NULL, " +
            " taskId INTEGER, FOREIGN KEY (taskId) REFERENCES task(id) ON DELETE CASCADE);";

    public static final String DELETE_TASK_PATTERN = "DELETE FROM " + TABLE_NAME + " " +
            "WHERE id = %d;";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private final DbHelper dbHelper;
    private final NotificationManager notificationManager;

    public ReminderRepository(DbHelper dbHelper, NotificationManager notificationManager) {
        this.dbHelper = dbHelper;
        this.notificationManager = notificationManager;
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
//        writableDatabase.execSQL(DROP_TABLE);
        writableDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public Reminder save(Reminder object) {
        ContentValues values = new ContentValues();
        if (object.getId() != null) {
            values.put("id", object.getId());
        }
        values.put("time", object.getTime().toString());
        values.put("taskId", object.getTaskId());
        long insert = dbHelper.getWritableDatabase().replaceOrThrow(TABLE_NAME, null, values);
        notificationManager.scheduleNotification(object.getTime().getTime() - System.currentTimeMillis(), (int) insert,
                "Please do " + object.getTaskName() + "!.");
        return object.setId(insert);
    }

    @Override
    public List<Reminder> findAll() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return parseResults(cursor);
    }

    @Override
    public void delete(long id) {
        String query = String.format(DELETE_TASK_PATTERN, id);
        notificationManager.cancelNotification((int) id);
        dbHelper.getWritableDatabase().execSQL(query);
    }

    public List<Reminder> findForTask(long taskId) {
        List<Reminder> all = findAll();
        Iterator<Reminder> iterator = all.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getTaskId() != taskId) {
                iterator.remove();
            }
        }
        return all;
    }

    private List<Reminder> parseResults(Cursor response) {
        List<Reminder> items = new ArrayList<>();
        int idIndex = response.getColumnIndex("id");
        int nameIndex = response.getColumnIndex("time");
        int taskIndex = response.getColumnIndex("taskId");
        while (response.moveToNext()) {
            items.add(new Reminder()
                    .setId(response.getInt(idIndex))
                    .setTime(new Date(response.getString(nameIndex)))
                    .setTaskId(response.getInt(taskIndex)));
        }
        return items;
    }
}
