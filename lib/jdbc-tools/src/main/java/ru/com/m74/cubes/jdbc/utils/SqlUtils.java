package ru.com.m74.cubes.jdbc.utils;

import ru.com.m74.cubes.jdbc.annotations.Column;
import ru.com.m74.cubes.jdbc.annotations.LinkTo;
import ru.com.m74.cubes.jdbc.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        if (Utils.isEmpty(columns)) {
//            if (field.isAnnotationPresent(Nsi.class)) {
//                return new String[]{field.getName() + "_TEXT"};
//            } else
            if (field.isAnnotationPresent(LinkTo.class)) {
                LinkTo linkTo = field.getAnnotation(LinkTo.class);
                return new String[]{field.getName() + "_" + linkTo.title()};
            } else {
                return new String[]{getColumnName(type, field)};
            }
        } else {
            return columns;
        }
    }

    public static String aliasName(String alias) {
//        return "\"" + alias + "\"";
        return alias;
    }

    public static String tableAlias(Table tna) {
        if (Utils.isNotEmpty(tna.alias())) return tna.alias();
        if (Utils.isEmpty(tna.value())) return null;
        String arr[] = tna.value().split(" ");
        return arr.length == 2 ? arr[1] : null;
    }

    public static String tableName(Table tna) {
        if (Utils.isEmpty(tna.value())) return null;
        if (Utils.isEmpty(tna.alias())) return tna.value().split(" ")[0];
        return tna.value();
    }

    public static <T> String getColumnName(Class<T> type, Field field) {
        Table tna = type.getAnnotation(Table.class);
        Column rsfa = field.getAnnotation(Column.class);
        String column = rsfa.value();
        if (column.contains(".")) return column;

        String alias = Utils.isEmpty(rsfa.alias()) ? tableAlias(tna) : rsfa.alias();
        if (Utils.isNotEmpty(alias)) {
            column = aliasName(alias) + "." + column;
        }
        return column;
    }


//    public static Field getPrimaryKeyField(Class<?> type) {
//        return DTOUtils.getPrimaryKeyField(type);
//    }

    public static String getResultSetFieldName(Field field) {
        Column rsf = field.getAnnotation(Column.class);
        if (Utils.isNotEmpty(rsf.as())) {
            return rsf.as();
        }

        String value = rsf.value();
        if (Utils.isNotEmpty(value)) {
            int i = value.lastIndexOf('.');
            return i != -1 ? value.substring(i + 1) : value;
        }

        return field.getName();
    }

    public static String getFieldAlias(Column rsf) {
        int i = rsf.value().indexOf('.');
        return i != -1 ? rsf.value().substring(0, i) : rsf.alias();
    }

    private static <T> Method findSetter(Class<T> dtoClass, Field field) throws NoSuchMethodException {
        return dtoClass.getDeclaredMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), field.getType());
    }

    public static <T> boolean isFieldInTable(Class<T> dtoClass, Field field) {
        try {
            findSetter(field.getDeclaringClass(), field);
            Column rsf = field.getAnnotation(Column.class);
            if (Utils.isEmpty(rsf.value())) return false;
            String fieldAlias = getFieldAlias(rsf);
            return Utils.isEmpty(fieldAlias) || Utils.isEquals(tableAlias(dtoClass.getAnnotation(Table.class)), fieldAlias);
        } catch (NoSuchMethodException e) {
            return false;
        }

    }
}
