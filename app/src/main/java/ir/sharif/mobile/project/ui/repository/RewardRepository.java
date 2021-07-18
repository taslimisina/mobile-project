package ir.sharif.mobile.project.ui.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

import ir.sharif.mobile.project.ui.model.Reward;

public class RewardRepository extends SQLiteOpenHelper implements BaseRepository<Reward> {

    public RewardRepository(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public Reward save(Reward object) {
        return null;
    }

    @Override
    public List<Reward> findAll() {
        return null;
    }

    @Override
    public Reward findById(Long id) {
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
