package ru.com.m74.cubes.jdbc.repository;

import java.util.Map;

public interface AbstractRepo<T, I> extends ReadOnlyRepo<T, I> {

    T save(T entity);

    T persist(T entity);

    T save(I id, Map<String, Object> changes);

    void deleteById(I id);

    void deleteByIds(I[] ids);

    void convert(Map<String, Object> changes);
}
