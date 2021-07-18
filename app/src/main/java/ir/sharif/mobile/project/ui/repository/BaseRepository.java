package ir.sharif.mobile.project.ui.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {

    T save(T object);

    List<T> findAll();

    Optional<T> findById(Long id);
}
