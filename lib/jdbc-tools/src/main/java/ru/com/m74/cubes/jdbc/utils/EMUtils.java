package ru.com.m74.cubes.jdbc.utils;

import org.springframework.lang.Nullable;
import ru.com.m74.cubes.jdbc.ColumnNotFoundException;
import ru.com.m74.cubes.jdbc.Link;
import ru.com.m74.cubes.jdbc.annotations.LinkTo;
import ru.com.m74.cubes.sql.base.Select;
import ru.com.m74.extjs.dto.Sorter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static ru.com.m74.cubes.common.ObjectUtils.*;
import static ru.com.m74.cubes.jdbc.utils.SqlUtils.getResultSetFieldName;

public class EMUtils {
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
//    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    public static <T> void sort(Select<T> q, Sorter sorters[]) {
        forEach(sorters, sorter -> {
            q.addOrderBy(SqlUtils.getOrderBy(q.getType(), sorter.getProperty()), sorter.getDirection());
        });
    }

    public static Class<?> getFilterType(Class<?> cls) {
        if (Link.class.isAssignableFrom(cls)) return Long.class;
        if (Enum.class.isAssignableFrom(cls)) return String.class;
        return cls;
    }

    /**
     * Получить значение из базы
     *
     * @param rs
     * @param field
     * @return
     * @throws SQLException
     */
    public static Object getResultSetValue(ResultSet rs, Field field) throws ColumnNotFoundException {

        Class fieldType = field.getType();
        String colName = getResultSetFieldName(field);

        if (field.isAnnotationPresent(LinkTo.class)) {
            LinkTo annotation = field.getAnnotation(LinkTo.class);
            if (fieldType.equals(Link.class)) {
                Object id = get(rs, field.getName() + "Id");
                if (id == null) return null;
                String title = get(rs, field.getName() + "Title", String.class);
                Link item = new Link(id, title);
                if (isNotEmpty(annotation.bk())) {
                    item.setBusinessKey(get(rs, field.getName() + "Bk", String.class));
                }
                return item;
            }
            throw new RuntimeException("Не совместимы тип: " + fieldType);
        }

        if (fieldType.equals(int.class)) {
            fieldType = Integer.class;
        } else if (fieldType.equals(boolean.class)) {
            fieldType = Boolean.class;
        } else if (fieldType.equals(long.class)) {
            fieldType = Long.class;
        } else if (fieldType.equals(double.class)) {
            fieldType = Double.class;
        } else if (fieldType.equals(UUID.class)) {
            return UUID.fromString(get(rs, colName, String.class));
        }

        return fieldType.equals(Object.class) ? get(rs, colName) : get(rs, colName, fieldType);
    }

    private static Object get(ResultSet rs, String name) throws ColumnNotFoundException {
        return get(rs, name, null);
    }

    private static <T> T get(ResultSet rs, String name, Class<T> type) throws ColumnNotFoundException {
        try {
            return type != null ? rs.getObject(name, type) : (T) rs.getObject(name);
        } catch (SQLException e) {
            throw new ColumnNotFoundException(name, e);
        }
    }

    /**
     * Преобразовать строковое значение к указанному типу
     *
     * @param value значение
     * @param cls   тип
     * @param <T>
     * @return
     */
    public static <T> T cast(@Nullable Object value, Class<T> cls) {

        if (cls.equals(boolean.class) && isEmpty(value)) {
            value = false;
        }

        if (isEmpty(value)) {
            return null;
        }

        if (cls.isInstance(value)) {
            return (T) value;
        }

//        if (!(value instanceof String || value instanceof Number || value instanceof Boolean)) {
//            throw new RuntimeException("Not a string, number or boolean value: " + value);
//        }

        if (cls.equals(Date[].class)) {
            List list = (List) value;
            Date[] arr = new Date[list.size()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = cast(list.get(i), Date.class);
            }
            return (T) arr;
        }

        String strVal = value.toString();

        if (cls.equals(Date.class)) {
            try {
                SimpleDateFormat timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
                return (T) timestampFormat.parse(strVal);
            } catch (ParseException e) {
                throw new RuntimeException("Ошибка при конвертации строки в дату", e);
            }
        }

        if (cls.equals(Long.class) || cls.equals(long.class)) {
            try {
                if (value instanceof Double) {
                    return (T) new Long(((Double) value).longValue());
                } else {
                    return (T) Long.valueOf(strVal);
                }
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в число", e);
            }
        }
        if (cls.equals(Integer.class) || cls.equals(int.class)) {
            try {
                return (T) Integer.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в число", e);
            }
        }
        if (cls.equals(Double.class) || cls.equals(double.class)) {
            try {
                return (T) Double.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в число", e);
            }
        }
        if (cls.equals(Boolean.class) || cls.equals(boolean.class)) {
            try {
                return (T) Boolean.valueOf(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в логический тип", e);
            }
        }
        if (cls.equals(BigDecimal.class)) {
            try {
                return (T) new BigDecimal(strVal);
            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при конвертации строки в большое число", e);
            }
        }

        throw new RuntimeException("Unsupported type: " + cls);
    }
}
