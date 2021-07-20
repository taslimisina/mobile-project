package ir.sharif.mobile.project.ui.repository;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ir.sharif.mobile.project.ui.model.ChecklistItem;

public interface BaseRepository<T> {

    String DB_NAME = "TASK_MANAGEMENT";

    int DB_VERSION = 1;

    T save(T object);

    List<T> findAll();

    void delete(long id);
}
