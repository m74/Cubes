package ru.com.m74.cubes.jdbc.utils;

import ru.com.m74.cubes.jdbc.ColumnNotFoundException;
import ru.com.m74.cubes.jdbc.Link;
import ru.com.m74.cubes.jdbc.annotations.Column;
import ru.com.m74.cubes.jdbc.annotations.LinkTo;
import ru.com.m74.cubes.sql.base.Select;
import ru.com.m74.extjs.dto.Filter;
import ru.com.m74.extjs.dto.Sorter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;
import static ru.com.m74.cubes.common.ObjectUtils.*;
import static ru.com.m74.cubes.jdbc.utils.DTOUtils.findField;
import static ru.com.m74.cubes.jdbc.utils.SqlUtils.getColumnNameWithAlias;
import static ru.com.m74.cubes.jdbc.utils.SqlUtils.getResultSetFieldName;
import static ru.com.m74.extjs.dto.Filter.Operator.between;

public class EMUtils {
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
//    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    public static <T> void sort(Select<T> q, Sorter sorters[]) {
        forEach(sorters, sorter -> {
            q.addOrderBy(SqlUtils.getOrderBy(q.getType(), sorter.getProperty()), sorter.getDirection());
        });
    }

    private static Class<?> getFilterType(Class<?> cls) {
        if (Link.class.isAssignableFrom(cls)) return Long.class;
        if (Enum.class.isAssignableFrom(cls)) return String.class;
        return cls;
    }

    public static <T> void filter(Select<T> q, Map<String, Object> params, Filter filters[]) {
        AtomicInteger i = new AtomicInteger();

        forEach(filters, filter -> {
            String fname = filter.getProperty();

            Field field = findField(q.getType(), fname);
            if (field != null && field.getAnnotation(Column.class) != null && isNotEmpty(filter.getValue())) {
                String pname = fname + i.getAndIncrement();

                if (filter.getOperator() == between) {
                    Date[] period = requireNonNull(cast(filter.getValue(), Date[].class), "Не указан период");
                    params.put(pname + "From", period[0]);
                    params.put(pname + "Till", period[1]);
                } else {
                    params.put(pname, cast(filter.getValue(), getFilterType(field.getType())));
                }

                String cname = getColumnNameWithAlias(q.getType(), field);

                if (String.class.isAssignableFrom(field.getType()) || Enum.class.isAssignableFrom(field.getType())) {
                    switch (filter.getOperator()) {
//                        case like:
//                            q.and(cname + " like :" + fname);
//                            break;
//                        case starts:
//                            q.and(cname + " like :" + fname + "||'%'");
//                            break;
//                        case ends:
//                            q.and(cname + " like '%'||:" + fname);
//                            break;
//                        case contains:
//                            q.and(cname + " like '%'||:" + fname + "||'%'");
//                            break;
                        case eq:
                            q.and(cname + " = :" + fname);
                            break;
                        case like:
                            q.and("upper(" + cname + ") like upper(:" + fname + ")");
                            break;
                        case starts:
                            q.and("upper(" + cname + ") like upper(:" + fname + ")||'%'");
                            break;
                        case ends:
                            q.and("upper(" + cname + ") like '%'||upper(:" + fname + ")");
                            break;
                        case contains:
                            // Для поиска совместимого со старой программой. Поиск в учете, поле "Автор документа"
                            q.and(cname + " IS NOT NULL");
                            q.and("CATSEARCH(" + cname + ", :" + fname + ", NULL) > 0");
//                            q.and("upper(" + cname + ") like '%'||upper(:" + fname + ")||'%'");
                            break;
                    }
                } else if (Link.class.isAssignableFrom(field.getType())) {
                    q.and(cname + " " + filter.getOperator().sql() + " :" + pname);
                } else if (Date.class.isAssignableFrom(field.getType()) || Number.class.isAssignableFrom(field.getType())) {
                    switch (filter.getOperator()) {
                        case between:
                            q.and(cname + " between :" + pname + "From and :" + pname + "Till");
                            break;
                        default:
                            q.and(cname + " " + filter.getOperator().sql() + " :" + pname);
                    }
                } else {
                    throw new RuntimeException(field.getName());
                }
            }
        });
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
    public static <T> T cast(Object value, Class<T> cls) {

        if (cls.equals(boolean.class) && isEmpty(value)) {
            value = false;
        }

        if (cls.isInstance(value)) {
            return (T) value;
        }

        if (isEmpty(value)) {
            return null;
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
