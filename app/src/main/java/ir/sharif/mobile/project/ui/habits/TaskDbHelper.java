package ir.sharif.mobile.project.ui.habits;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TaskDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tasks.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "habit";
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "title VARCHAR(150) NOT NULL, " +
            " description VARCHAR(500), " + " reward INTEGER NOT NULL);";

    public static final String INSERT_TASK_PATTERN = "REPLACE INTO " + TABLE_NAME + " " +
            "(title, description, reward) VALUES (\"%s\", \"%s\", %d);";

    public static final String DELETE_TASK_PATTERN = "DELETE FROM " + TABLE_NAME + " " +
            "WHERE id = %d;";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public TaskDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
        // TODO: add migrations if needed.
    }

    public void truncate() {
        getWritableDatabase().execSQL(DROP_TABLE);
        getWritableDatabase().execSQL(CREATE_TABLE_QUERY);
    }

    public void delete(Task task) {
        String query = String.format(DELETE_TASK_PATTERN, task.getId());
        getWritableDatabase().execSQL(query);
    }

    public void insert(Task task) {
        String query = String.format(INSERT_TASK_PATTERN, task.getTitle(), task.getDescription(), task.getReward());
        getWritableDatabase().execSQL(query);
    }

    public List<Task> getAllMatched(String title) {
        Cursor response;
        if (title == null || title.isEmpty()) {
            response = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        } else {
            response = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME
                    + " WHERE title LIKE \"%" + title + "%\";", null);
        }
        return parseResults(response);
    }

    private List<Task> parseResults(Cursor response) {
        List<Task> tasks = new ArrayList<>();
        int idIndex = response.getColumnIndex("id");
        int titleIndex = response.getColumnIndex("title");
        int descriptionIndex = response.getColumnIndex("description");
        int rewardIndex = response.getColumnIndex("reward");
        while (response.moveToNext()) {
            tasks.add(new Task(response.getInt(idIndex), response.getString(titleIndex),
                    response.getString(descriptionIndex), response.getInt(rewardIndex)));
        }
        return tasks;
    }
}
