package ir.sharif.mobile.project.ui.repository;

import java.util.List;

public interface BaseRepository<T> {

    String DB_NAME = "TASK_MANAGEMENT";
    int DB_VERSION = 1;

    T save(T object);

    List<T> findAll();

    T findById(Long id);
}
