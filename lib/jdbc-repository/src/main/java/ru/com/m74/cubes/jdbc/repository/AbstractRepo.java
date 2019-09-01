package ru.com.m74.cubes.jdbc.repository;

import ru.com.m74.extjs.dto.Request;

import java.util.List;
import java.util.Map;

public interface AbstractRepo<T> {

    List<T> getAll(Request request);

    List<T> getAll(Request request, Map<String, Object> params);

    long count(Request request, Map<String, Object> params);

    T get(Object id);

    T save(T entity);

    T persist(T entity);

    T save(Object id, Map<String, Object> changes);

    void deleteById(Object id);

//    void deleteByIds(Object[] ids);
}
