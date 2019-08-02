package ru.com.m74.cubes.jdbc.repository;

import ru.com.m74.extjs.dto.Pagination;

import java.util.Map;

public interface AbstractRepo<T> {

    Iterable<T> getAll(Pagination pagination);

    long count();

    T get(Object id);

    T save(T entity);

    T save(Object id, Map<String, Object> changes);

    void deleteById(Object id);

//    void deleteByIds(Object[] ids);
}
