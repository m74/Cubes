package ru.com.m74.cubes.sql.base;

/**
 * @author mixam
 * @since 02.07.16 0:35
 */
public class Delete {
    private String table;

    private Where where;

    public Delete(String table) {
        this.table = table;
    }

    /**
     * Обнулить условия и добавить новое
     *
     * @param str
     * @return
     */
    public Delete where(String str) {
        this.where = new Where(str);
        return this;
    }

    public Delete and(String... str) {
        where.and(str);
        return this;
    }

    @Override
    public String toString() {
        return "delete from " + table + " " + where;
    }
}
