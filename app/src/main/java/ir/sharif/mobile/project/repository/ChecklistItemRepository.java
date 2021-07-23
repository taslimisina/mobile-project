package ir.sharif.mobile.project.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ir.sharif.mobile.project.model.ChecklistItem;

public class ChecklistItemRepository implements BaseRepository<ChecklistItem> {

    private static final String TABLE_NAME = "checklist";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + " name VARCHAR(150) NOT NULL, " +
            " taskId INTEGER, checked INTEGER, FOREIGN KEY (taskId) REFERENCES task(id) ON DELETE CASCADE);";

    public static final String DELETE_TASK_PATTERN = "DELETE FROM " + TABLE_NAME + " " +
            "WHERE id = %d;";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private final DbHelper dbHelper;


    public ChecklistItemRepository(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
//        writableDatabase.execSQL(DROP_TABLE);
        writableDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public synchronized ChecklistItem save(ChecklistItem object) {
        ContentValues values = new ContentValues();
        if (object.getId() != null) {
            values.put("id", object.getId());
        }
        values.put("name", object.getName());
        values.put("checked", object.isChecked() ? 1 : 0);
        values.put("taskId", object.getTaskId());
        long insert = dbHelper.getWritableDatabase().replaceOrThrow(TABLE_NAME, null, values);
        return object.setId(insert);
    }

    @Override
    public List<ChecklistItem> findAll() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return parseResults(cursor);
    }

    @Override
    public void delete(long id) {
        String query = String.format(DELETE_TASK_PATTERN, id);
        dbHelper.getWritableDatabase().execSQL(query);
    }

    public List<ChecklistItem> findForTask(long taskId) {
        List<ChecklistItem> all = findAll();
        Iterator<ChecklistItem> iterator = all.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getTaskId() != taskId) {
                iterator.remove();
            }
        }
        return all;
    }

    private List<ChecklistItem> parseResults(Cursor response) {
        List<ChecklistItem> items = new ArrayList<>();
        int idIndex = response.getColumnIndex("id");
        int nameIndex = response.getColumnIndex("name");
        int checkedIndex = response.getColumnIndex("checked");
        int taskIndex = response.getColumnIndex("taskId");
        while (response.moveToNext()) {
            items.add(new ChecklistItem()
                    .setId(response.getInt(idIndex))
                    .setName(response.getString(nameIndex))
                    .setChecked(response.getInt(checkedIndex) == 1)
                    .setTaskId(response.getInt(taskIndex)));
        }
        return items;
    }
}
