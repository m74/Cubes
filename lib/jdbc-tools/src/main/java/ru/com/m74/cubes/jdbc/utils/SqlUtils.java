package ru.com.m74.cubes.jdbc.utils;

import ru.com.m74.cubes.jdbc.annotations.Column;
import ru.com.m74.cubes.jdbc.annotations.LinkTo;
import ru.com.m74.cubes.jdbc.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import static ru.com.m74.cubes.common.ObjectUtils.*;
import static ru.com.m74.cubes.common.StringUtils.camel2snake;

/**
 * @author mixam
 * @since 24.11.17 0:26
 */
public class SqlUtils {

    /**
     * Получить поле сортировки
     *
     * @param type
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> String[] getOrderBy(Class<T> type, String fieldName) {
        Field field = DTOUtils.findField(type, fieldName);
        Column rsfa = field.getAnnotation(Column.class);
        String columns[] = rsfa.orderBy();
        if (isEmpty(columns)) {
            if (field.isAnnotationPresent(LinkTo.class)) {
                LinkTo linkTo = field.getAnnotation(LinkTo.class);
                return new String[]{field.getName() + "_" + linkTo.title()};
            } else {
                return new String[]{getColumnNameWithAlias(type, field)};
            }
        } else {
            return columns;
        }
    }

    public static String aliasName(String alias) {
//        return "\"" + alias + "\"";
        return alias;
    }

    public static String tableAlias(Class<?> type) {
        Table tna = type.getAnnotation(Table.class);
        if (tna == null) {
            throw new RuntimeException("Annotation not present: " + Table.class);
        }

//        if (isNotEmpty(tna.alias())) return tna.alias();
        if (isEmpty(tna.value())) return null;
        String arr[] = tna.value().split(" ");
        return arr.length == 2 ? arr[1] : null;
    }

    public static String tableName(Class<?> type) {
        Table a = type.getAnnotation(Table.class);
        if (a != null && isNotEmpty(a.value()))
            return a.value().split(" ")[0];
        else return camel2snake(type.getSimpleName());
    }

    public static <T> String getColumnNameWithAlias(Class<T> type, String fieldName) {
        return getColumnNameWithAlias(type, Objects.requireNonNull(DTOUtils.findField(type, fieldName)));
    }

    public static String getColumnName(Field field) {
        Column col = field.getAnnotation(Column.class);
        String cname = col.value();
        if (isEmpty(cname)) cname = camel2snake(field.getName());
        return cname;
    }

    public static <T> String getColumnNameWithAlias(Class<T> type, Field field) {
        Column col = field.getAnnotation(Column.class);
        String column = col.value();
        if (isEmpty(column)) {
            column = camel2snake(field.getName());
        } else if (column.contains(".")) {
            return column;
        }

        String alias = getFieldAlias(col);
        if (isEmpty(alias)) alias = tableAlias(type);
        if (isNotEmpty(alias)) {
            column = alias + "." + quote(column);
        }
        return column;
    }

    public static String quote(String name) {
        return "\"" + name + "\"";
    }


//    public static Field getPrimaryKeyField(Class<?> type) {
//        return DTOUtils.getPrimaryKeyField(type);
//    }

    public static String getResultSetFieldName(Field field) {
//        Column rsf = field.getAnnotation(Column.class);
//        if (isNotEmpty(rsf.as())) {
//            return rsf.as();
//        }

//        String value = rsf.value();
//        if (isNotEmpty(value)) {
//            int i = value.lastIndexOf('.');
//            return i != -1 ? value.substring(i + 1) : value;
//        }

        return field.getName();
    }

    public static String getFieldAlias(Column rsf) {
        int i = rsf.value().indexOf('.');
        return i != -1 ? rsf.value().substring(0, i) : null;
    }

    private static <T> Method findSetter(Class<T> dtoClass, Field field) throws NoSuchMethodException {
        return dtoClass.getDeclaredMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), field.getType());
    }

    public static <T> boolean isFieldInTable(Class<T> type, Field field) {
        try {
            findSetter(field.getDeclaringClass(), field);
        } catch (NoSuchMethodException e) {
            return false;
        }
        Column col = field.getAnnotation(Column.class);
        if (isNotEmpty(col.sql())) return false;
        String fieldAlias = getFieldAlias(col);
        return isEmpty(fieldAlias) || isEquals(tableAlias(type), fieldAlias);
    }

}
