package ru.com.m74.cubes.jdbc;

import java.sql.SQLException;

/**
 * @author mixam
 * @since 28.04.18 13:19
 */
public class ColumnNotFoundException extends SQLException {

    public ColumnNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
