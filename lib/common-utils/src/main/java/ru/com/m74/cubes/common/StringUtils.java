package ru.com.m74.cubes.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.com.m74.cubes.common.ObjectUtils.isNotEmpty;

public class StringUtils {

    /**
     * Конвертировать UPPER_CASE в CamelCase
     *
     * @param s
     * @return
     */
    public static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String result = null;
        for (String part : parts) {
            result = result == null ? part.toLowerCase() : result + toProperCase(part);
        }
        return result;
    }

    /**
     * Убирает из строки лишние пробелы
     *
     * @param str
     * @return
     */
    public static String removeRedundantSpaces(String str) {
        if (str == null) return null;
        return str.trim().replaceAll("\\s{2,}", " ");
    }

    private static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    public static String trim(String string) {
        return string != null ? string.trim() : null;
    }

    public static String trimSpaces(String str, String wordDelimiter) {
        List<String> arr = new ArrayList<>();
        for (String word : str.split(wordDelimiter)) {
            if (isNotEmpty(word)) arr.add(word);
        }
        return String.join(wordDelimiter, arr);
    }

    public static String format(String str, Object o) {
        return format(str, o, " ");
    }

    public static String format(String str, Object o, String delimiter) {
        if (o instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) o;
            for (String name : map.keySet()) {
                Object v = map.get(name);
                str = str.replace("{" + name + "}", v != null ? v.toString() : "");
            }
        } else {
            try {
                for (Method m : o.getClass().getDeclaredMethods()) {
                    String name = m.getName();
                    if (name.matches("^(get|is).*")) {
                        name = name.replaceAll("^(get|is)", "");
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);

                        Object v = m.invoke(o);
                        str = str.replace("{" + name + "}", v != null ? v.toString() : "");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return trimSpaces(str, delimiter);
    }
}
