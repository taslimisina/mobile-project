package ir.sharif.mobile.project.ui.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Reminder;

public class ReminderRepository extends SQLiteOpenHelper implements BaseRepository<Reminder> {

    private static final String TABLE_NAME = "remider";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "time datetime(3) NOT NULL, " +
            " taskId INTEGER, CONSTRAINT taskId FOREIGN KEY (id) REFERENCES task);";

    public static final String DELETE_TASK_PATTERN = "DELETE FROM " + TABLE_NAME + " " +
            "WHERE id = %d;";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public ReminderRepository(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    @Override
    public Reminder save(Reminder object) {
        ContentValues values = new ContentValues();
        if (object.getId() != null) {
            values.put("id", object.getId());
        }
        values.put("time", object.getTime().toString());
        values.put("taskId", object.getTaskId());
        long insert = getWritableDatabase().replaceOrThrow(TABLE_NAME, null, values);
        return object.setId(insert);
    }

    @Override
    public List<Reminder> findAll() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return parseResults(cursor);
    }

    @Override
    public void delete(long id) {
        String query = String.format(DELETE_TASK_PATTERN, id);
        getWritableDatabase().execSQL(query);
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
