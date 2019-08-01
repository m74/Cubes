package ru.com.m74.extjs;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 */
public class Utils {
    /**
     * Получить primary key field
     *
     * @param type
     * @return
     */
    public static Field getId(Class type) {
        List<Field> fields = getModelFields(type, field -> field.isAnnotationPresent(Id.class));
        if (fields.isEmpty()) throw new RuntimeException("@Id not present in: " + type);
        return fields.get(0);

    }

    /**
     * Получить все свойства модели кроме статических
     *
     * @param type       класс модели
     * @param predicates Дополнительные условия фильтрации
     * @return список свойств
     */
    public static List<Field> getModelFields(Class type, Predicate<Field>... predicates) {
        List<Field> fields =
                type.getSuperclass().equals(Object.class) ?
                        new ArrayList<>() :
                        getModelFields(type.getSuperclass(), predicates);

        Stream<Field> stream = Arrays.stream(type.getDeclaredFields());
        stream = stream.filter(field -> !Modifier.isStatic(field.getModifiers()));

        for (Predicate<Field> predicate : predicates) {
            stream = stream.filter(predicate);
        }

        Collections.addAll(fields, stream.toArray(Field[]::new));

        return fields;
    }



    /**
     * @param entity
     * @param changes
     */
    public static void applyChanges(Object entity, Map<String, Object> changes) {
        Class type = entity.getClass();

        for (String fieldName : changes.keySet()) {
            Object val = changes.get(fieldName);
            if (isEmpty(val)) continue;

            try {
                Field f = type.getDeclaredField(fieldName);
                if (f == null || f.isAnnotationPresent(Id.class)) continue;

                set(entity, f, cast(val, f.getType()));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param entity
     * @param field
     * @param value
     */
    public static void set(Object entity, Field field, Object value) {
        boolean isPrivate = !field.isAccessible();
        if (isPrivate) field.setAccessible(true);
        try {
            field.set(entity, value);
            if (isPrivate) field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Преобразовать строковое значение к указанному типу
     *
     * @param value значение
     * @param type  тип
     * @param <T>
     * @return
     */
    private static <T> T cast(Object value, Class<T> type) {

        if (type.equals(boolean.class) && isEmpty(value)) {
            value = false;
        }

        if (type.isInstance(value)) {
            return (T) value;
        }

        if (isEmpty(value)) {
            return null;
        }

        if (!(value instanceof String || value instanceof Number || value instanceof Boolean)) {
            throw new RuntimeException("Not a string, number or boolean value: " + value);
        }

        if (type.equals(Timestamp.class)) {
            return (T) new Timestamp(cast(value, Date.class).getTime());
        }

        String strVal = value.toString();

        if (type.equals(Date.class)) {
            try {
                SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                return (T) timestampFormat.parse(strVal);
            } catch (ParseException e) {
                throw new RuntimeException("Ошибка при конвертации строки в дату", e);
            }
        }
        if (type.equals(Long.class) || type.equals(long.class)) {
            try {
                return (T) Long.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в число", e);
            }
        }
        if (type.equals(Integer.class) || type.equals(int.class)) {
            try {
                return (T) Integer.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в число", e);
            }
        }
        if (type.equals(Double.class) || type.equals(double.class)) {
            try {
                return (T) Double.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в число", e);
            }
        }
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            try {
                return (T) Boolean.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в логический тип", e);
            }
        }
        if (type.equals(BigDecimal.class)) {
            try {
                return (T) new BigDecimal(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в большое число", e);
            }
        }

        throw new RuntimeException("Unsupported type: " + type);
    }

    /**
     * Пустой ли объект
     *
     * @param o Строка,Массив,Число
     * @return
     */

    public static boolean isEmpty(Object o) {
        if (o instanceof Collection<?>)
            return ((Collection<?>) o).isEmpty();
        if (o instanceof String)
            return ((String) o).isEmpty();
        if (o instanceof Number)
            return ((Number) o).intValue() == 0;
        return o == null;
    }
}
