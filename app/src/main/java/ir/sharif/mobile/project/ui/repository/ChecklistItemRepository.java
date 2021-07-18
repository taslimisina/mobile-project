package ir.sharif.mobile.project.ui.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

import ir.sharif.mobile.project.ui.model.ChecklistItem;

public class ChecklistItemRepository extends SQLiteOpenHelper
        implements BaseRepository<ChecklistItem> {

    public ChecklistItemRepository(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public ChecklistItem save(ChecklistItem object) {
        return null;
    }

    @Override
    public List<ChecklistItem> findAll() {
        return null;
    }

    @Override
    public ChecklistItem findById(Long id) {
        return null;
    }
}
