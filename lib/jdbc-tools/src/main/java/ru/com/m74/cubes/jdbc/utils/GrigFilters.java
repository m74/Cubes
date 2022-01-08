package ru.com.m74.cubes.jdbc.utils;

import ru.com.m74.cubes.common.ObjectUtils;
import ru.com.m74.cubes.jdbc.Link;
import ru.com.m74.cubes.jdbc.annotations.Column;
import ru.com.m74.cubes.sql.base.Select;
import ru.com.m74.extjs.dto.Filter;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.com.m74.cubes.common.ObjectUtils.isNotEmpty;
import static ru.com.m74.cubes.jdbc.utils.DTOUtils.findField;
import static ru.com.m74.cubes.jdbc.utils.EMUtils.cast;
import static ru.com.m74.cubes.jdbc.utils.EMUtils.getFilterType;
import static ru.com.m74.cubes.jdbc.utils.SqlUtils.getColumnNameWithAlias;

public class GrigFilters {
    private static GrigFilters instance = null;

    public static GrigFilters getInstance() {
        if (instance == null) instance = new GrigFilters();
        return instance;
    }

    public static void setInstance(GrigFilters instance) {
        GrigFilters.instance = instance;
    }

    public <T> void filter(Select<T> q, Map<String, Object> params, Filter filters[]) {
        AtomicInteger i = new AtomicInteger();

        ObjectUtils.forEach(filters, filter -> {
            String fname = filter.getProperty();

            Field field = findField(q.getType(), fname);
            if (field != null && field.getAnnotation(Column.class) != null && isNotEmpty(filter.getValue())) {
                String pname = fname + i.getAndIncrement();

                if ("between".equals(filter.getOperator())) {
                    Date[] period = Objects.requireNonNull(cast(filter.getValue(), Date[].class), "Не указан период");
                    params.put(pname + "From", period[0]);
                    params.put(pname + "Till", period[1]);
                } else {
                    params.put(pname, cast(filter.getValue(), getFilterType(field.getType())));
                }

                String cname = getColumnNameWithAlias(q.getType(), field);

                doFilter(q, filter, cname, fname, pname, field);
            }
        });
    }

    protected <T> void doStringFilter(Select<T> q, Filter filter, String cname, String fname) {
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
            case "eq":
                q.and(cname + " = :" + fname);
                break;
            case "like":
                q.and("upper(" + cname + ") like upper(:" + fname + ")");
                break;
            case "starts":
                q.and("upper(" + cname + ") like upper(:" + fname + ")||'%'");
                break;
            case "ends":
                q.and("upper(" + cname + ") like '%'||upper(:" + fname + ")");
                break;
            case "contains":
                q.and("upper(" + cname + ") like '%'||upper(:" + fname + ")||'%'");
                break;
            case "containsWords":
                for (String word : ((String) filter.getValue()).split(" ")) {
                    q.and("upper(" + cname + ") like upper('" + word + "')||' %' or " +
                            "upper(" + cname + ") like '% '||upper('" + word + "') or " +
                            "upper(" + cname + ") like '% '||upper('" + word + "')||' %'");
                }
                break;
        }
    }

    private  <T> void doFilter(Select<T> q, Filter filter, String cname, String fname, String pname, Field field) {
        if (String.class.isAssignableFrom(field.getType()) || Enum.class.isAssignableFrom(field.getType())) {
            doStringFilter(q, filter, cname, fname);
        } else if (Link.class.isAssignableFrom(field.getType())) {
            q.and(cname + " " + getOperatorSql(filter.getOperator()) + " :" + pname);
        } else if (Date.class.isAssignableFrom(field.getType())) {
            switch (filter.getOperator()) {
                case "between":
                    q.and(cname + " between :" + pname + "From and :" + pname + "Till");
                    break;
                default:
                    q.and("trunc(" + cname + ") " + getOperatorSql(filter.getOperator()) + " :" + pname);
            }
        } else if (Number.class.isAssignableFrom(field.getType())) {
            q.and(cname + " " + getOperatorSql(filter.getOperator()) + " :" + pname);
        } else {
            throw new RuntimeException(field.getName());
        }
    }

    public String getOperatorSql(String operator) {
        switch (operator) {
            case "like":
            case "starts":
            case "ends":
            case "contains":
//                case likeIC:
//                case startsIC:
//                case endsIC:
//                case containsIC:
                return "like";
            case "eq":
                return "=";
            case "lt":
            case "before":
                return "<";
            case "after":
            case "gt":
                return ">";
        }
        throw new RuntimeException(operator);
    }
}
