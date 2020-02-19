package ru.com.m74.cubes.sql;

public class MySqlDialect implements Dialect {

    @Override
    public String pagging(String sql, String delim) {
        return sql + delim + "LIMIT :limit OFFSET :start";
    }
}
