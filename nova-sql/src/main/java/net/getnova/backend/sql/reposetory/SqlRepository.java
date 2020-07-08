package net.getnova.backend.sql.reposetory;

import java.util.List;
import java.util.stream.Stream;

public interface SqlRepository<T, K> {

    List<T> list();

    Stream<T> stream();

    T find(K key);

    T save(T entity);

    /**
     * Deletes the entity with the specified primary key.
     *
     * @param key the primary key of the entity, which should be deleted
     * @return if the entity with the primary key was found and deleted
     */
    boolean delete(K key);
}
