package ru.com.m74.cubes.jdbc.utils;

import ru.com.m74.cubes.sql.base.Select;
import ru.com.m74.extjs.dto.Sorter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author mixam
 * @since 21.04.17 10:38
 */
public class Utils {

//    protected final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static MapUtil<String, Object> map() {
        return new MapUtil<>();
    }

    public static MapUtil<String, Object> map(String key, Object val) {
        return new MapUtil<>(key, val);
    }

    public static class MapUtil<K, V> extends HashMap<K, V> {
        private MapUtil(K key, V val) {
            add(key, val);
        }

        private MapUtil() {
        }

        public MapUtil<K, V> add(K key, V val) {
            put(key, val);
            return this;
        }
    }

    /**
     * Определяем  идентичность двух аргументов
     *
     * @param val1
     * @param val2
     * @return
     */
    public static boolean isEquals(Object val1, Object val2) {
        return val1 != null && val2 != null && val1.equals(val2);
    }

    /**
     * Значение аргумента пусто
     *
     * @param val
     * @return
     */
    public static boolean isEmpty(Object val) {
        if (val == null) {
            return true;
        } else if (val instanceof String) {
            return ((String) val).isEmpty();
        } else if (val instanceof Map) {
            return ((Map) val).isEmpty();
        } else if (val instanceof Collection) {
            return ((Collection) val).isEmpty();
        } else if (val instanceof Object[]) {
            return ((Object[]) val).length == 0;
        } else {
            return false;
        }
    }


    /**
     * Имеет ли аргумент значение
     *
     * @param val
     * @return
     */
    public static boolean isNotEmpty(Object val) {
        return !isEmpty(val);
    }

    /**
     * Конвертировать UPPER_CASE в CamelCase
     *
     * @param s
     * @return
     */
//    public static String toCamelCase(String s) {
//        String[] parts = s.split("_");
//        String result = null;
//        for (String part : parts) {
//            result = result == null ? part.toLowerCase() : result + toProperCase(part);
//        }
//        return result;
//    }

    // убирает из строки лишние пробелы
//    public static String removeRedundantSpaces(String str) {
//        if (str == null) return null;
//        return str.trim().replaceAll("\\s{2,}", " ");
//    }

//    private static String toProperCase(String s) {
//        return s.substring(0, 1).toUpperCase() +
//                s.substring(1).toLowerCase();
//    }

//    public static String trim(String string) {
//        return string != null ? string.trim() : null;
//    }

//    public static String trimSpaces(String str, String wordDelimiter) {
//        List<String> arr = new ArrayList<>();
//        for (String word : str.split(wordDelimiter)) {
//            if (isNotEmpty(word)) arr.add(word);
//        }
//        return String.join(wordDelimiter, arr);
//    }
//
//    public static String format(String str, Object o) {
//        return format(str, o, " ");
//    }
//
//    public static String format(String str, Object o, String delimiter) {
//        if (o instanceof Map) {
//            Map<String, Object> map = (Map<String, Object>) o;
//            for (String name : map.keySet()) {
//                Object v = map.get(name);
//                str = str.replace("{" + name + "}", v != null ? v.toString() : "");
//            }
//        } else {
//            try {
//                for (Method m : o.getClass().getDeclaredMethods()) {
//                    String name = m.getName();
//                    if (name.matches("^(get|is).*")) {
//                        name = name.replaceAll("^(get|is)", "");
//                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
//
//                        Object v = m.invoke(o);
//                        str = str.replace("{" + name + "}", v != null ? v.toString() : "");
//                    }
//                }
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return trimSpaces(str, delimiter);
//    }
//    /**
//     * @param q
//     * @param type
//     * @param sort
//     * @return
//     */
//    public static Select applySorters(Select q, Class<?> type, Sorter[] sort) {
//        if (Utils.isNotEmpty(sort)) {
//            for (Sorter sorter : sort) {
//                q.addOrderBy(SqlUtils.getOrderBy(type, sorter.getProperty()), sorter.getDirection());
//            }
//        }
//        return q;
//    }
//
}
