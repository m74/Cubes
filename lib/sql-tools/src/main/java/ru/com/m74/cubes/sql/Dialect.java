package ru.com.m74.cubes.sql;

public interface Dialect {

    String pagging(String sql, String delim);
}
