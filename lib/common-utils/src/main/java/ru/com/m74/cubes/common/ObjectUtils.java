package ru.com.m74.cubes.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class ObjectUtils {

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
     * Определяем  идентичность двух аргументов
     *
     * @param val1
     * @param val2
     * @return
     */
    public static boolean isEquals(Object val1, Object val2) {
        return val1 != null && val2 != null && val1.equals(val2);
    }

    public static <T> void forEach(T arr[], Consumer<T> consumer) {
        if (isNotEmpty(arr)) forEach(Arrays.asList(arr), consumer);
    }

    public static <T> void forEach(Iterable<T> arr, Consumer<T> consumer) {
        if (isNotEmpty(arr)) arr.forEach(consumer);
    }
}
