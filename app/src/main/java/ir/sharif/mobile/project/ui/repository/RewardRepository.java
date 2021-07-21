package ir.sharif.mobile.project.ui.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ir.sharif.mobile.project.ui.model.Reward;

public class RewardRepository implements BaseRepository<Reward> {

    private static final String TABLE_NAME = "reward";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "title VARCHAR(150) NOT NULL, " +
            " amount INTEGER, description text);";

    public static final String DELETE_TASK_PATTERN = "DELETE FROM " + TABLE_NAME + " " +
            "WHERE id = %d;";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private final DbHelper dbHelper;

    public RewardRepository(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
//        writableDatabase.execSQL(DROP_TABLE);
        writableDatabase.execSQL(CREATE_TABLE_QUERY);
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
        long insert = dbHelper.getWritableDatabase().replaceOrThrow(TABLE_NAME, null, values);
        return object.setId(insert);
    }

    @Override
    public List<Reward> findAll() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return parseResults(cursor);
    }

    @Override
    public void delete(long id) {
        String query = String.format(DELETE_TASK_PATTERN, id);
        dbHelper.getWritableDatabase().execSQL(query);
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
