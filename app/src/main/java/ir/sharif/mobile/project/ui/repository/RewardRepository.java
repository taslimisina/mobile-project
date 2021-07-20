package ir.sharif.mobile.project.ui.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Reward;

public class RewardRepository extends SQLiteOpenHelper implements BaseRepository<Reward> {

    private static final String TABLE_NAME = "reward";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "title VARCHAR(150) NOT NULL, " +
            " amount INTEGER, description text);";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public RewardRepository(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public Reward save(Reward object) {
        ContentValues values = new ContentValues();
        if (object.getId() != null) {
            values.put("id", object.getId());
        }
        values.put("title", object.getTitle());
        values.put("description", object.getDescription());
        values.put("amount", object.getAmount());
        long insert = getWritableDatabase().replaceOrThrow(TABLE_NAME, null, values);
        return object.setId(insert);
    }

    @Override
    public List<Reward> findAll() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return parseResults(cursor);
    }

    private List<Reward> parseResults(Cursor response) {
        List<Reward> items = new ArrayList<>();
        int idIndex = response.getColumnIndex("id");
        int titleIndex = response.getColumnIndex("title");
        int desIndex = response.getColumnIndex("description");
        int amountIndex = response.getColumnIndex("amount");
        while (response.moveToNext()) {
            items.add(new Reward()
                    .setId(response.getInt(idIndex))
                    .setTitle(response.getString(titleIndex))
                    .setDescription(response.getString(desIndex))
                    .setAmount(response.getInt(amountIndex)));
        }
        return items;
    }
}
