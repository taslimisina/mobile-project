package ir.sharif.mobile.project.repository;

import java.util.List;

public interface BaseRepository<T> {

    int DB_VERSION = 1;

    T save(T object);

    List<T> findAll();

    void delete(long id);
}
