package ir.sharif.mobile.project.ui.repository;

import android.app.Presentation;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.Calendar;
import java.util.List;

public class CoinRepository {

    public static final String TABLE_NAME = "coin";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "time datetime(3) NOT NULL, " +
            " score INTEGER NOT NULL);";

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

    public synchronized int getLastScore() {
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT * from " + TABLE_NAME + " ORDER BY id DESC", null, null);
        int scoreIndex = cursor.getColumnIndex("score");
        cursor.moveToFirst();
        return cursor.getInt(scoreIndex);
    }

}
