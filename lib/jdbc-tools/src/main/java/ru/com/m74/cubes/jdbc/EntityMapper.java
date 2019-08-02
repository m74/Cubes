package ru.com.m74.cubes.jdbc;

import java.sql.ResultSet;

public interface EntityMapper<T> {
    void map(T entity, ResultSet resultSet, int i);
}
