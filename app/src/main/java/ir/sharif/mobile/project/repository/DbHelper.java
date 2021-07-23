package ir.sharif.mobile.project.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class DbHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, "TASK_MANAGEMENT", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
