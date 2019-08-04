package ru.com.m74.cubes.sql.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mixam
 * @since 02.07.16 0:18
 */
public class Update {
    private String table;
    private List<String> values = new ArrayList<>();
    private Where where = null;

    public Update(String table) {
        this.table = table;
    }

    public Update set(String column, Object value) {
        values.add(column + "=" + String.valueOf(value));
        return this;
    }

    /**
     * Обнулить условия и добавить новое
     *
     * @param str
     * @return
     */
    public Update where(String str) {
        this.where = new Where(str);
        return this;
    }

    public Update and(String... str) {
        where.and(str);
        return this;
    }

    public boolean isNotEmpty() {
        return !values.isEmpty();
    }


    public String toString(String delim) {
        return "update " + table + " set" + delim + String.join(delim + ",", values) + " " + where;
    }

    public String toPrettyString() {
        return toString("\n");
    }

    @Override
    public String toString() {
        return toString(" ");
    }
}
