package ru.com.m74.cubes.jdbc.repository;

import ru.com.m74.extjs.dto.Request;

import java.util.List;
import java.util.Map;

public interface ReadOnlyRepo<T, I> {
    List<T> getAll(Request request);

    List<T> getAll(Request request, Map<String, Object> params);

    long count(Request request, Map<String, Object> params);

    T get(I id);
}
