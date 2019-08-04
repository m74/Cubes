package ru.com.m74.cubes.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.com.m74.cubes.jdbc.annotations.*;
import ru.com.m74.cubes.jdbc.utils.DTOUtils;
import ru.com.m74.cubes.jdbc.utils.SqlUtils;
import ru.com.m74.cubes.sql.base.Insert;
import ru.com.m74.cubes.sql.base.Select;
import ru.com.m74.cubes.sql.base.Update;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.com.m74.cubes.common.MapUtils.map;
import static ru.com.m74.cubes.common.ObjectUtils.isEmpty;
import static ru.com.m74.cubes.common.ObjectUtils.isNotEmpty;
import static ru.com.m74.cubes.jdbc.utils.DTOUtils.*;
import static ru.com.m74.cubes.jdbc.utils.EMUtils.cast;
import static ru.com.m74.cubes.jdbc.utils.EMUtils.getResultSetValue;
import static ru.com.m74.cubes.jdbc.utils.SqlUtils.*;

public class EntityManager {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public EntityManager(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> Select<T> select(Class<T> type) {
        Select<T> select = new Select<>();

        Table table = type.getAnnotation(Table.class);
        if (table == null) {
            throw new RuntimeException("Annotation not present: " + Table.class);
        }

        String tableAlias = tableAlias(table);

        String tableName = tableName(table);
        select.from(isEmpty(tableAlias) ? tableName : tableName + " " + aliasName(tableAlias));

        Join join = type.getAnnotation(Join.class);
        if (join != null) {
            select.join(join.value());
        }

        for (Field field : DTOUtils.getAnnotatedModelFields(type)) {
//            Id id = field.getAnnotation(Id.class);
//            if (id != null && isNotEmpty(id.name())) {
//                setHint("/*+ INDEX (" + tableName + " " + id.name() + ") */");
//            }

            Column rsfa = field.getAnnotation(Column.class);

            if (isNotEmpty(rsfa.sql())) {
                select.field("(" + rsfa.sql() + ") as " + (isNotEmpty(rsfa.as()) ? rsfa.as() : field.getName()));
            } else {
                String column = getColumnName(type, field);
                if (field.isAnnotationPresent(LinkTo.class)) {
                    if (field.getType().equals(Link.class)) {
                        LinkTo annotation = field.getAnnotation(LinkTo.class);
                        String alias = annotation.as();
                        if (isEmpty(alias)) alias = aliasName(field.getName());

                        if (isEmpty(annotation.on())) {
                            select.join(
                                    "left join " + annotation.table() + " " + alias + " " +
                                            "ON(" + alias + "." + annotation.id() + "=" + column + ")");
                        } else {
                            select.join(
                                    "left join " + annotation.table() + " " + alias + " " + "ON(" + annotation.on() + ")");
                        }
                        select.field(alias + "." + annotation.id() + " as " + field.getName() + "_" + annotation.id());
                        String query = annotation.titleQuery();
                        if (isEmpty(query)) {
                            query = alias + "." + annotation.title();
                        }
                        select.field("(" + query + ") as " + field.getName() + "_" + annotation.title());
                        if (isNotEmpty(annotation.bk())) {
                            select.field(alias + "." + annotation.bk() + " as " + field.getName() + "_" + annotation.bk());
                        }
                    }
                } else {
                    String as = rsfa.as();
                    if (isNotEmpty(as)) {
                        select.field(column + " as " + rsfa.as());
                    } else {
                        select.field(column);
                    }
                }
            }
        }
//        return new ru.com.m74.cubes.jdbc.sql.Select<>(type);
        return select;
    }

    private Insert insert(Class<?> type, boolean withId) {
        Insert q = new Insert();
        Table table = type.getAnnotation(Table.class);

        if (table == null)
            throw new RuntimeException("Annotation not present: " + Table.class);

        q.into(tableName(table));

        for (Field field : DTOUtils.getAnnotatedModelFields(type)) {
            if (field.isAnnotationPresent(Id.class) && !withId) continue;

            Column rsfa = field.getAnnotation(Column.class);
            if (isNotEmpty(rsfa.value()) && rsfa.value().indexOf('.') == -1) {
                q.value(SqlUtils.getResultSetFieldName(field), ":" + field.getName());
            }
        }

        return q;
    }


    private SqlParameterSource generateSqlValues(Object dto) {
        Class<?> type = dto.getClass();

        Map<String, Object> values = new HashMap<>();
        for (Field field : DTOUtils.getAnnotatedModelFields(type)) {
            values.put(field.getName(), getValue(dto, field));
        }
        return new MapSqlParameterSource(values);
    }


    public void persist(Object dto) {
        Class<?> type = dto.getClass();
        SqlParameterSource namedParameters = generateSqlValues(dto);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        Field idField = getPrimaryKeyField(type);
        if (idField == null) throw new RuntimeException("@PrimaryKey annotation not present in: " + type);
        Object idValue = getValue(dto, idField);
        jdbcTemplate.update(insert(type, idValue != null).toString(), namedParameters, keyHolder, new String[]{SqlUtils.getResultSetFieldName(idField)});

        // safe PK setting
        final Class<?> idFieldType = idField.getType();
        if (idFieldType.isAssignableFrom(Long.class) || idFieldType.equals(long.class)) {
            //if long
            if (keyHolder.getKey() != null)
                set(dto, idField, keyHolder.getKey().longValue());
        } else if (idFieldType.isAssignableFrom(BigDecimal.class)) {
            // if big number
            if (keyHolder.getKey() != null)
                set(dto, idField, new BigDecimal(keyHolder.getKey().toString()));
        } else {
            throw new IllegalStateException(
                    MessageFormat.format("Неподдерживаемый тип PK поля ({1}) для сущности {0}",
                            dto.getClass().getName(), idFieldType.getName()));
        }

    }


    private <T> Update update(Class<T> type, Map<String, Object> values) {
        Table table = type.getAnnotation(Table.class);

        if (table == null)
            throw new RuntimeException("Annotation not present: " + Table.class);

        Update q = new Update(tableName(table));

        for (String key : values.keySet()) {
            Field field = DTOUtils.findField(type, key);

            if (field != null) {
                if (field.isAnnotationPresent(Id.class)) {
                    // skip PK
                    continue;
                }

                if (field.isAnnotationPresent(Column.class)) {
                    Column rsf = field.getAnnotation(Column.class);
                    if (isNotEmpty(rsf.value()) && (isEmpty(SqlUtils.getFieldAlias(rsf)))) {
                        q.set(rsf.value(), ":" + field.getName());
                    }
                }
            }
        }

        Field primaryKeyField = getPrimaryKeyField(type);
        q.where(primaryKeyField.getAnnotation(Column.class).value() + "=:" + primaryKeyField.getName());

        return q;
    }


    public void update(Class clazz, Object idValue, Map<String, Object> values) {
        values = new HashMap<>(values);

        for (String key : values.keySet()) {
            if (isEmpty(values.get(key))) continue;

            Field field = DTOUtils.findField(clazz, key);
            if (field == null
                    // только для привязанных к базе
                    || !field.isAnnotationPresent(Column.class)
                    // не изменяем ID
                    || field.isAnnotationPresent(Id.class)) continue;

            Class<?> c = field.getType();
//            if (c.equals(AttachmentDTO.class)) {
//                if (values.get(key) instanceof Map)
//                    values.put(key, ((Map) values.get(key)).get("id"));
//            }

//            if (field.isAnnotationPresent(DictionaryValue.class) && values.get(key) instanceof Map) {
//                Map map = (Map) values.get(key);
//                if (map.get("value") != null) {
//                    values.put(key, map.get("value"));
//                } else {
//                    Enum en = Utils.cast(((Map) values.get(key)).get("businessKey"), (Class<Enum>) field.getAnnotation(DictionaryValue.class).value());
//                    //values.put(key, catalogDao.getIdByBusinessKey(en));
//                    values.put(key, nsiDao.getIdByBusinessKey(en));
//                }
//                continue;
//            }
            if (c == List.class || c == Map.class) continue;

//            if (ListItemDTO.class.isAssignableFrom(c)) {
//                c = Long.class;
//                // если с фронта пришел { text: '', value: ''}
//                if (values.get(key) instanceof Map) {
//                    values.put(key, ((Map) values.get(key)).get("value"));
//                }
//            }

            if (field.isAnnotationPresent(LinkTo.class)) {
                c = field.getAnnotation(LinkTo.class).idType();
            }

            if (c == Date.class && values.get(key).getClass() == Date.class) continue;

            if (c == Boolean.class || c == boolean.class) {
                values.put(key, cast(values.get(key), Boolean.class) ? 1 : 0);
                continue;
            }

            values.put(key, cast(values.get(key), c));
        }

        Update q = update(clazz, values);
        if (q.isNotEmpty()) {
            values.put(DTOUtils.getPrimaryKeyField(clazz).getName(), idValue);
            jdbcTemplate.update(q.toPrettyString(), values);
        }
    }

    public <T> T createEntity(ResultSet rs, Class<T> type) {
        try {
            if (Number.class.isAssignableFrom(type) ||
                    Date.class.isAssignableFrom(type) ||
                    type.equals(Boolean.class) ||
                    type.equals(String.class)
                    ) {
                return rs.getObject(1, type);
            } else {
//                long time = System.currentTimeMillis();
                T dto = type.newInstance();
                for (Field field : DTOUtils.getAnnotatedModelFields(type)) {
                    try {
                        set(dto, field, getResultSetValue(rs, field));
                    } catch (ColumnNotFoundException ignored) {
                    }
                }
//                LOG.debug("Class:{}, Time:{}", type, (System.currentTimeMillis() - time));
                return dto;
            }
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Long count(Select<T> q, Map<String, Object> params) {
        return jdbcTemplate.queryForObject(q.getCountSql(), params, Long.class);
    }

    public <T> Iterable<T> getResultList(String q, Map<String, Object> params, RowMapper<T> mapper) {
        return jdbcTemplate.query(q, params, mapper);
    }

    public <T> Iterable<T> getResultList(Select<T> q, Map<String, Object> params) {
        return getResultList(q.toString(), params, (resultSet, i) -> createEntity(resultSet, q.getType()));
    }


    public void save(Object dto) {
        Field primaryKeyField = getPrimaryKeyField(dto.getClass());
        Object idValue = getValue(dto, primaryKeyField);
        if (idValue == null) {
            persist(dto);
        } else {
            update(dto);
        }
    }


    public void update(Object dto) {
//        singleQueriesUpdate(dto);
        multiQueriesUpdate(dto);
    }

    private void singleQueriesUpdate(Object dto) {
        Class<?> dtoClass = dto.getClass();

        String tableName = SqlUtils.tableName(dtoClass.getAnnotation(Table.class));

        Update sql = new Update(tableName);

        Map<String, Object> values = new HashMap<>();

        Field primaryKeyField = getPrimaryKeyField(dtoClass);

        for (Field field : getModelAnnotatedFieldsWithoutId(dtoClass)) {
            if (primaryKeyField == field) continue;
            if (SqlUtils.isFieldInTable(dtoClass, field)) {

                String sqlFieldName = SqlUtils.getResultSetFieldName(field);
                String fieldName = field.getName();
                Object value = getValue(dto, field);
                values.put(fieldName, value);

                sql.set(sqlFieldName, ":" + fieldName);
            }
        }

        sql.where(SqlUtils.getResultSetFieldName(primaryKeyField) + "=:" + primaryKeyField.getName());

        values.put(primaryKeyField.getName(), getValue(dto, primaryKeyField));

//        LOG.debug("sql: {}", sql);
//        LOG.debug("values: {}", values);

        jdbcTemplate.update(sql.toString(), values);
    }

    private void multiQueriesUpdate(Object dto) {
        Class<?> dtoClass = dto.getClass();
        String tableName = getTableName(dtoClass);
//        String tableAlias = dtoClass.getAnnotation(TableName.class).alias();

        Field primaryKeyField = getPrimaryKeyField(dtoClass);
        String sqlPrimaryKeyName = SqlUtils.getResultSetFieldName(primaryKeyField);
        Object idValue = getValue(dto, primaryKeyField);

        for (Field field : getModelAnnotatedFieldsWithoutId(dtoClass)) {
            if (primaryKeyField == field) continue;
            if (SqlUtils.isFieldInTable(dtoClass, field)) {
                Object value = getValue(dto, field);
                jdbcTemplate.update("UPDATE " + tableName + " set " + SqlUtils.getResultSetFieldName(field) + "=:value where " + sqlPrimaryKeyName + "=:id", map().add("value", value).add("id", idValue));
            }
        }
    }

    private static String getTableName(Class<?> dtoClass) {
        Table tableNameAnnotation = dtoClass.getAnnotation(Table.class);
        if (tableNameAnnotation == null) {
            throw new IllegalStateException("Table name can't be determined " +
                    "because it is not annotated using @TableName");
        }
        return tableName(tableNameAnnotation);
    }

    /**
     * Все поля модели аннотированные как @ResultsetField исключая @Id
     *
     * @param dtoClass
     * @return
     */
    private static List<Field> getModelAnnotatedFieldsWithoutId(Class<?> dtoClass) {
        return DTOUtils.getModelFields(dtoClass,
                field -> field.isAnnotationPresent(Id.class)
                        || field.isAnnotationPresent(Column.class));
    }

    public <T> T get(Class<T> type, Object id) {
        return get(type, DTOUtils.getPrimaryKeyField(type), id);
    }

    private <T> T get(Class<T> type, Field field, Object value) {
        return getSingleResult(select(type).where(SqlUtils.getColumnName(type, field) + "=:id"), map("id", value));
    }

//    public <T> T getSingleResult(Select<T> query, Map<String, Object> params, RowMapper<T> mapper) {
//        return jdbcTemplate.queryForObject(
//                query.toString(), params, mapper);
//    }

    public <T> T getSingleResult(Select<T> query, Map<String, Object> params) {
        return jdbcTemplate.queryForObject(
                query.toString(), params, (resultSet, i) -> createEntity(resultSet, query.getType()));
    }

    public void remove(Class type, Object idValue) {
        Field primaryKeyField = getPrimaryKeyField(type);
        String primaryKeyFieldName = SqlUtils.getResultSetFieldName(primaryKeyField);
        String tableName = getTableName(type);

        jdbcTemplate.update("DELETE " + tableName + " WHERE " + primaryKeyFieldName + " = :id", map("id", idValue));
    }

//    public void remove(Object dto) {
//        Field primaryKeyField = getPrimaryKeyField(dto.getClass());
//        if (primaryKeyField == null) throw new RuntimeException("PrimaryKey not fount: " + dto.getClass());
//        remove(dto.getClass(), getValue(dto, primaryKeyField));
//    }
}
