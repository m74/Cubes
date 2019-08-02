package ru.com.m74.cubes.sql.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mixam
 * @since 01.07.16 23:50
 */
public class Where implements Serializable {
    private List<String> conditions = new ArrayList<>();

    public Where(String... conditions) {
        and(conditions);
    }

    public void and(String... conditions) {
        for (String condition : conditions) {
            this.conditions.add("(" + condition + ")");
        }
    }

    public String toString(String delim) {
        return "where" + delim + String.join(delim + "and ", conditions);

    }

    @Override
    public String toString() {
        return toString(" ");
    }
}
