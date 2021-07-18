package ir.sharif.mobile.project.ui.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

import ir.sharif.mobile.project.ui.model.Reminder;

public class ReminderRepository extends SQLiteOpenHelper implements BaseRepository<Reminder> {

    public ReminderRepository(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public Reminder save(Reminder object) {
        return null;
    }

    @Override
    public List<Reminder> findAll() {
        return null;
    }

    @Override
    public Reminder findById(Long id) {
        return null;
    }
}
