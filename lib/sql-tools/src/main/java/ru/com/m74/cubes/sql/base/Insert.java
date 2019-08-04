package ru.com.m74.cubes.sql.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mixam
 * @since 02.07.16 0:03
 */
public class Insert {
    private String table;
    private List<String> fields = new ArrayList<>();
    private List<String> values = new ArrayList<>();

    public Insert into(String table) {
        this.table = table;
        return this;
    }

    public Insert value(String key, Object value) {
        fields.add(key);
        values.add(String.valueOf(value));
        return this;
    }

    @Override
    public String toString() {
        return toString(" ");
    }

    public String toString(String delim) {
        return "insert into" + delim + table + "(" + delim + String.join("," + delim, fields) + delim + ") values(" + delim + String.join("," + delim, values) + delim + ")";
    }

    public String toPrettyString() {
        return toString("\n");
    }
}
