package ru.com.m74.cubes.jdbc.utils;

import ru.com.m74.cubes.jdbc.annotations.Column;
import ru.com.m74.cubes.jdbc.annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Утилиты для работы с DTO
 *
 * @author mixam
 * @since 28.07.16 0:00
 */
public class DTOUtils {

    public static Object get(Object dto, Field field) {
        try {
            boolean isPrivate = !field.isAccessible();
            if (isPrivate) field.setAccessible(true);
            Object value = field.get(dto);
            if (isPrivate) field.setAccessible(false);
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void set(Object dto, Field field, Object value) {
        try {
            boolean isPrivate = !field.isAccessible();
            if (isPrivate) field.setAccessible(true);
            field.set(dto, value);
            if (isPrivate) field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить список полей DTO, аннотированных как @Column
     * В том числе и в суперклассе
     *
     * @param type класс модели
     * @return список свойств
     */
    public static List<Field> getAnnotatedModelFields(Class type) {
        return getModelFields(type, field -> field.isAnnotationPresent(Column.class));
    }

    /**
     * Получить primary key field
     *
     * @param type
     * @return
     */
    public static Field getPrimaryKeyField(Class type) {
        List<Field> fields = getModelFields(type, field -> field.isAnnotationPresent(Id.class));
        return fields.isEmpty() ? null : fields.get(0);

    }

    /**
     * Получить все свойства модели кроме статических
     *
     * @param type       класс модели
     * @param predicates Дополнительные условия фильтрации
     * @return список свойств
     */
    public static List<Field> getModelFields(Class type, Predicate<Field>... predicates) {
        Class superType = type.getSuperclass();

        List<Field> fields = superType.equals(Object.class) ?
                new ArrayList<>() :
                getModelFields(type.getSuperclass(), predicates);

        Stream<Field> stream = Arrays.stream(type.getDeclaredFields());
        stream = stream.filter(field -> !Modifier.isStatic(field.getModifiers()));

        for (Predicate<Field> predicate : predicates) {
            stream = stream.filter(predicate);
        }
//       stream.collect(Collectors.toList());
        Collections.addAll(fields, stream.toArray(Field[]::new));

        return fields;

    }

    /**
     * Найти свойство в классе либо в предках
     *
     * @param type Класс
     * @param name Имя свойства
     * @return Свойство
     */
    public static Field findField(Class type, String name) {
        try {
            return type.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            type = type.getSuperclass();
            return type == null ? null : findField(type, name);
        }
    }

    public static Object getValue(Object dto, Field field) {
        Object value = get(dto, field);

        if (value == null) {
            return null;
        }

        if (field.isAnnotationPresent(Id.class) && value instanceof Number && ((Number) value).intValue() <= 0) {
            return null;
        }

        if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        }

        if (field.getType().isEnum()) {
            return ((Enum<?>) value).name();
        }

        return value;
    }
//
//    public static <T> Map getColumnsMap(Class<T> type) {
//        Map<String, Object> map = map();
//
//        forEach(getAnnotatedModelFields(type), field -> {
//            String name = field.getName();
//            map.put(name, SqlUtils.getColumnNameWithAlias(type, name));
//        });
//        return map;
//    }
//
}
