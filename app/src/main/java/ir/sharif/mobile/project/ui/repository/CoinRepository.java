package ir.sharif.mobile.project.ui.repository;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CoinRepository {

    public static final String TABLE_NAME = "coin";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "time datetime(3) NOT NULL, " +
            " score INTEGER NOT NULL);";

    public static final String DELETE_PATTERN = "DELETE FROM " + TABLE_NAME + " " +
            "WHERE id = %d;";

    private DbHelper dbHelper;

    public CoinRepository(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        dbHelper.getWritableDatabase().execSQL(CREATE_TABLE_QUERY);
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * from " + TABLE_NAME, null, null);
        if (!cursor.moveToNext()) {
            ContentValues values = new ContentValues();
            values.put("time", Calendar.getInstance().getTime().toString());
            values.put("score", 0);
            dbHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
        }
    }

    public synchronized int increase(int amount) {
        ContentValues values = new ContentValues();
        int lastScore = getLastScore();
        values.put("time", Calendar.getInstance().getTime().toString());
        values.put("score", lastScore + amount);
        dbHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
        return lastScore + amount;
    }

    public synchronized int decrease(int amount) {
        ContentValues values = new ContentValues();
        int lastScore = getLastScore();
        values.put("time", Calendar.getInstance().getTime().toString());
        values.put("score", lastScore - amount);
        dbHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
        return lastScore - amount;
    }

    public synchronized int set(int amount) {
        ContentValues values = new ContentValues();
        values.put("time", Calendar.getInstance().getTime().toString());
        values.put("score", amount);
        dbHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
        return amount;
    }

    public synchronized int undo() {
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * from " + TABLE_NAME + " ORDER BY id DESC", null, null);
        int idIndex = cursor.getColumnIndex("id");
        cursor.moveToFirst();
        int id = cursor.getInt(idIndex);
        dbHelper.getWritableDatabase().execSQL(String.format(DELETE_PATTERN, id));
        return getLastScore();
    }

    public synchronized int getLastScore() {
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * from " + TABLE_NAME + " ORDER BY id DESC", null, null);
        int scoreIndex = cursor.getColumnIndex("score");
        cursor.moveToFirst();
        return cursor.getInt(scoreIndex);
    }

    public Map<Date, Integer> getHistory() {
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * from " + TABLE_NAME + " ORDER BY id DESC", null, null);
        int scoreIndex = cursor.getColumnIndex("score");
        int timeIndex = cursor.getColumnIndex("time");
        HashMap<Date, Integer> history = new HashMap<>();
        while (cursor.moveToNext()) {
            int score = cursor.getInt(scoreIndex);
            Date date = new Date(cursor.getString(timeIndex));
            history.put(date, score);
        }
        return history;
    }
}
