package ru.com.m74.cubes.sql;

public class OracleDialect implements Dialect {

    @Override
    public String pagging(String sql, String delim) {
        // sql + delim + "OFFSET :start ROWS FETCH NEXT :limit ROWS ONLY";
        return "select t_.* from (" + delim + "select data_.*, rownum rn_" + delim + "from (" + delim +
                sql +
                delim + ") data_" + delim + "where rownum <= (:start + :limit)) t_ " + delim +
                "where t_.rn_ > :start";
    }
}
