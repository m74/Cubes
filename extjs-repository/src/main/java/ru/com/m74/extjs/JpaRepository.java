package ru.com.m74.extjs;

import ru.com.m74.extjs.dto.Pagination;

import java.util.Map;

public interface JpaRepository<T> {
    /**
     * @param id
     * @return
     */
    T get(Object id);

    /**
     * @return
     */
    Iterable<T> getAll(Pagination pagination);

    /**
     * @return
     */
    long count();

    /**
     * @param entity
     * @return
     */
    T save(T entity);

    /**
     * @param id
     * @param changes
     * @return
     */
    T save(Object id, Map<String, Object> changes);

    /**
     * @param entity
     */
    void delete(T entity);

    /**
     * @param id
     */
    void deleteById(Object id);

    /**
     *
     * @param ids
     */
    void deleteByIds(long ids[]);
}
