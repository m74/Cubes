package ru.com.m74.cubes.sql.base;

import ru.com.m74.cubes.sql.Dialect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Класс для построения SQL-запросов. Поддерживает построение эффективного для Oracle запроса постраничной выборки.
 * При опции paging = true метод toString формирует SQL следующего вида:
 * <pre>
 * select t_.*
 * from (
 *     select data_.*, rownum rn_
 *     from (<основной запрос>) data_
 *     where rownum <= (:start + :limit)) t_
 * where t_.rn_ > :start;
 * </pre>
 * При формировании count-запроса (метод toString, параметр count = true):
 * <ul>
 * <li>не действует paging</li>
 * <li>не добавляется order by</li>
 * <li>вместо списка полей выводится "count(*)"</li>
 * <li>при наличии distinct или group by запрос помещается во вложенный select с указанием полей выборки при distinct,
 * либо полей из group by</li>
 * </ul>
 * В count-запросе одноврменное указание DISTINCT и GROUP BY не поддерживается.
 *
 * @author mixam
 * @since 01.07.16 9:02
 */
public class Select<T> implements Serializable {
    private Class<T> type;
    private List<String> fields = new ArrayList<>();
    private String tableName = null;
    private List<String> joins = new ArrayList<>();
    private Where where = null;
    private String group = null;
    private List<String> orderBy = new ArrayList<>();
    private boolean pagging = false;
    private boolean distinct = false;
    //    private Select with = null;
//    private boolean count = false;
    private Dialect dialect;

    public Select(Dialect dialect) {
        this.dialect = dialect;
    }

    public Select(Dialect dialect, Class<T> type) {
        this(dialect);
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    /**
     * Очистить список полей и добавить новые
     */
    public Select<T> select(String... fields) {
        this.fields.clear();
        field(fields);
        return this;
    }

    /**
     * Добавить поля в результат запроса
     */
    public Select<T> field(String... fields) {
        Collections.addAll(this.fields, fields);
        return this;
    }

    public Select<T> from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Select<T> groupBy(String group) {
        this.group = group;
        return this;
    }

    public Select<T> orderBy(String orderBy) {
        this.orderBy.clear();
        if (orderBy != null)
            this.orderBy.add(orderBy);
        return this;
    }

    public Select<T> addOrderBy(String orderBy) {
        this.orderBy.add(orderBy);
        return this;
    }

    public Select<T> orderBy(String orderBy, String sortDirection) {
        this.orderBy.clear();
        if (orderBy != null)
            this.orderBy.add(orderBy + " " + sortDirection);
        return this;
    }

    public Select<T> addOrderBy(String orderBy, String sortDirection) {
        this.orderBy.add(orderBy + " " + sortDirection);
        return this;
    }

    public Select<T> addOrderBy(String columns[], String sortDirection) {
        for (String column : columns) {
            addOrderBy(column, sortDirection);
        }
        return this;
    }

    public Select<T> orderBy(String columns[], String sortDirection) {
        this.orderBy.clear();
        return addOrderBy(columns, sortDirection);
    }

    public Select<T> join(int index, String join) {
        this.joins.add(index, join);
        return this;
    }

    public Select<T> join(String... joins) {
        this.joins.addAll(Arrays.asList(joins));
        return this;
    }

    public Select<T> join(int index, String... joins) {
        this.joins.addAll(index, Arrays.asList(joins));
        return this;
    }

    public Where<T> getWhere() {
        return where;
    }

    /**
     * Обнулить условия и добавить новое
     */
    public Select<T> where(String... str) {
        return and(str);
    }

    /**
     * Добавить условие
     */
    public Select<T> and(String... str) {
        if (where != null)
            where.and(str);
        else
            this.where = new Where(str);

        return this;
    }


    public Select<T> setPagging(boolean pagging) {
        this.pagging = pagging;
        return this;
    }

    public Select<T> setDistinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

//    /**
//     * Является ли count-запросом (если true - не действует paging, не добавляется order by и вместо списка полей выводится "count(*)")
//     */
//    public Select<T> setCount(boolean count) {
//        this.count = count;
//        return this;
//    }

    public String toPrettyString() {
        return toString(false, "\n");
    }

    //    @Override
    public String toString() {
        return toString(false, " ");
    }

    public String getCountSql() {
        return toString(true, " ");
    }

    public String toString(boolean count, String delim) {
        if (tableName == null)
            throw new RuntimeException("TableName is null");

//        String sql = hint == null ? "" : hint + delim;
        String sql = "";
//        if (this.with != null) {
//            sql += "with q as (" + this.with.toString() + ")";
//        }

        if (count) {
            sql += "select count(*)" + delim;

            if (distinct && group != null)
                throw new RuntimeException("Одноврменное указание DISTINCT и GROUP BY не поддерживается");

            if (group != null || distinct) {
                sql += "from (select" + delim;

                if (distinct) {
                    sql += "distinct" + delim;
                    sql += (fields.isEmpty() ? "*" : String.join("," + delim, fields)) + delim;
                } else {
                    // group != null
                    sql += group + delim;
                }
            }
        } else {

            sql += "select" + delim;

            if (distinct)
                sql += "distinct" + delim;

            sql += (fields.isEmpty() ? "*" : String.join("," + delim, fields)) + delim;
        }

        sql += "from " + tableName;

        if (!joins.isEmpty())
            sql += delim + String.join(delim, joins);

        if (where != null)
            sql += delim + where.toString(delim);

        if (group != null)
            sql += delim + "group by " + group;

        if (count) {
            if (group != null || distinct)
                sql += delim + ")";
        } else {
            if (!orderBy.isEmpty())
                sql += delim + "order by " + String.join("," + delim, orderBy);

            if (pagging) sql = dialect.pagging(sql, delim);
        }

        return sql;
    }


//    public Select<T> with(Select with) {
//        this.with = with;
//        return this;
//    }
}
