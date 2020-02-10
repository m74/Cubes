package ru.com.m74.cubes.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

import static java.util.Objects.requireNonNull;
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

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public Select select(String... fields) {
        return new Select().select(fields);
    }

    public <T> Select<T> select(Class<T> type) {
        Select<T> select = new Select<>(type);

        String tableAlias = tableAlias(type);
        String tableName = tableName(type);

        select.from(isEmpty(tableAlias) ? tableName : tableName + " " + aliasName(tableAlias));


        for (Class t = type; t.getSuperclass() != null; t = t.getSuperclass()) {
            Join join = (Join) t.getAnnotation(Join.class);
            if (join != null) {
                select.join(join.value());
            }
        }
        for (Field field : DTOUtils.getAnnotatedModelFields(type)) {
//            Id id = field.getAnnotation(Id.class);
//            if (id != null && isNotEmpty(id.name())) {
//                setHint("/*+ INDEX (" + tableName + " " + id.name() + ") */");
//            }

            Column a = field.getAnnotation(Column.class);

            if (isNotEmpty(a.sql())) {
//                select.field("(" + a.sql() + ") as " + (isNotEmpty(a.as()) ? a.as() : field.getName()));
                select.field("(" + a.sql() + ") as " + quote(field.getName()));
            } else {
                String column = getColumnNameWithAlias(type, field);
                if (field.isAnnotationPresent(LinkTo.class)) {
                    if (field.getType().equals(Link.class)) {
                        LinkTo annotation = field.getAnnotation(LinkTo.class);
//                        String alias = annotation.as();
//                        if (isEmpty(alias)) alias = aliasName(field.getName());

                        String alias = quote(field.getName());

//                        if (isEmpty(annotation.on())) {
                        select.join(
                                "left join " + annotation.table() + " " + alias + " " +
                                        "ON(" + alias + "." + quote(annotation.id()) + "=" + column + ")");
//                        } else {
//                        select.join(
//                                "left join " + annotation.table() + " " + alias + " " + "ON(" + annotation.on() + ")");
//                        }
                        select.field(alias + "." + annotation.id() + " as " + quote(field.getName() + "Id"));
                        String query = annotation.titleQuery();
                        if (isEmpty(query)) {
                            query = alias + "." + annotation.title();
                        }
                        select.field("(" + query + ") as " + quote(field.getName() + "Title"));
                        if (isNotEmpty(annotation.bk())) {
                            select.field(alias + "." + annotation.bk() + " as " + quote(field.getName() + "Bk"));
                        }
                    }
                } else if (field.isAnnotationPresent(ManyToOne.class)) {

                } else {
//                    String as = a.as();
//                    if (isEmpty(as)) as = field.getName();
//                    select.field(column + " as " + quote(as));
                    select.field(column + " as " + quote(field.getName()));
                }
            }
        }
//        return new ru.com.m74.cubes.jdbc.sql.Select<>(type);
        return select;
    }

    private Insert insert(Class<?> type, Object dto) {
        Insert q = new Insert();
        q.into(tableName(type));

        for (Field field : getUpdatableFields(type)) {

            if (field.getAnnotation(Column.class).insertable() && getValue(dto, field) != null) {
                q.value(getColumnName(field), ":" + field.getName());
            }
        }

        return q;
    }

    private Map<String, Object> getValues(Object dto) {
        Class<?> type = dto.getClass();

        Map<String, Object> values = new HashMap<>();
        for (Field field : DTOUtils.getAnnotatedModelFields(type)) {
            Object v = getValue(dto, field);
            if (v !=null) values.put(field.getName(), v);
        }
        return values;
    }


    @SuppressWarnings({"unchecked"})
    public <T> T persist(T dto) {
        Class<T> type = (Class<T>) dto.getClass();

        Map<String, Object> values = getValues(dto);

        Insert q = insert(type, dto);
        Field idField = getPrimaryKeyField(type);
        if (idField == null || isNotEmpty(getValue(dto, idField))) {
            jdbcTemplate.update(q.toString(), new MapSqlParameterSource(values));
        } else {
//            throw new RuntimeException("@Id annotation not present in: " + type);
            Object idValue = getValue(dto, idField);
            String idColumnName = getColumnName(idField);
            if (idValue != null) {
                values.put(idField.getName(), idValue);
                q.value(idColumnName, ":" + idField.getName());
            }
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(q.toString(), new MapSqlParameterSource(values), keyHolder, new String[]{idColumnName});

            // safe PK setting
            final Class<?> idFieldType = idField.getType();
            if (idFieldType.isAssignableFrom(Long.class) || idFieldType.equals(long.class)) {
                //if long
                if (keyHolder.getKey() != null)
                    setValue(dto, idField, keyHolder.getKey().longValue());
            } else if (idFieldType.isAssignableFrom(BigDecimal.class)) {
                // if big number
                if (keyHolder.getKey() != null)
                    setValue(dto, idField, new BigDecimal(keyHolder.getKey().toString()));
            } else if (idFieldType.isAssignableFrom(String.class)) {
                // if string
//            if (keyHolder.getKey() != null)
//                setValue(dto, idField, keyHolder.getKey().toString());
            } else {
                throw new IllegalStateException(
                        MessageFormat.format("Неподдерживаемый тип PK поля ({1}) для сущности {0}",
                                dto.getClass().getName(), idFieldType.getName()));
            }
        }
//        refresh(dto);

        return dto;
    }

    private <T> Select<T> createSelectById(Class<T> type, Field idField) {
        return select(type).where(getColumnNameWithAlias(type, idField) + "=:id");
    }

    @SuppressWarnings({"unchecked"})
    public <T> void refresh(T entity) {
        Class<T> type = (Class<T>) entity.getClass();
        Field idField = requireNonNull(getPrimaryKeyField(type), "Требуется аннотация @Id");
        Object value = getValue(entity, idField);
        applyResult(createSelectById(type, idField), map("id", value), entity);
    }


    private <T> Update update(Class<T> type, Map<String, Object> values) {
        Update q = new Update(tableName(type));

        for (String key : values.keySet()) {
            Field field = DTOUtils.findField(type, key);

            if (field != null) {
                if (field.isAnnotationPresent(Id.class)) {
                    // skip PK
                    continue;
                }

                if (field.isAnnotationPresent(Column.class)) {
                    Column rsf = field.getAnnotation(Column.class);
                    if (rsf.updatable() && isEmpty(SqlUtils.getFieldAlias(rsf))) {
                        q.set(getColumnName(field), ":" + field.getName());
                    }
                }
            }
        }

        Field primaryKeyField = getPrimaryKeyField(type);
        if (primaryKeyField == null) throw new RuntimeException("@Id not found");
        q.where(getColumnName(primaryKeyField) + "=:" + primaryKeyField.getName());

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
            values.put(getPrimaryKeyField(clazz).getName(), idValue);
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
                T dto = type.newInstance();
                applyEntity(rs, dto);
                return dto;
            }
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void applyEntity(ResultSet rs, T dto) {
        for (Field field : DTOUtils.getAnnotatedModelFields(dto.getClass())) {
            try {
                setValue(dto, field, getResultSetValue(rs, field));
            } catch (ColumnNotFoundException ignored) {
            }
        }
    }

    public <T> Long count(Select<T> q, Map<String, Object> params) {
        return jdbcTemplate.queryForObject(q.getCountSql(), params, Long.class);
    }

    public <T> List<T> getResultList(Select<T> q, Map<String, Object> params, RowMapper<T> mapper) {
        return jdbcTemplate.query(q.toString(), params, mapper);
    }

    public <T> List<T> getResultList(Select<T> q, Map<String, Object> params) {
        return getResultList(q, params, (resultSet, i) -> createEntity(resultSet, q.getType()));
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
        singleQueriesUpdate(dto);
//        multiQueriesUpdate(dto);
    }

    private void singleQueriesUpdate(Object dto) {
        Class<?> type = dto.getClass();

        String tableName = tableName(type);

        Update sql = new Update(tableName);

        Map<String, Object> values = new HashMap<>();

        Field primaryKeyField = getPrimaryKeyField(type);

        for (Field field : getUpdatableFields(type)) {
            if (field.getAnnotation(Column.class).updatable()) {
                String fieldName = field.getName();
                values.put(fieldName, getValue(dto, field));
                sql.set(getColumnName(field), ":" + fieldName);
            }
        }

        sql.where(getColumnName(primaryKeyField) + "=:" + primaryKeyField.getName());

        values.put(primaryKeyField.getName(), getValue(dto, primaryKeyField));

        jdbcTemplate.update(sql.toString(), values);
    }

    private void multiQueriesUpdate(Object dto) {
        Class<?> type = dto.getClass();
        String tableName = tableName(type);
//        String tableAlias = type.getAnnotation(TableName.class).alias();

        Field primaryKeyField = getPrimaryKeyField(type);
        String sqlPrimaryKeyName = getColumnName(primaryKeyField);
        Object idValue = getValue(dto, primaryKeyField);

        for (Field field : getUpdatableFields(type)) {
            if (field.getAnnotation(Column.class).updatable()) {
                Object value = getValue(dto, field);
                jdbcTemplate.update("UPDATE " + tableName + " set " + getColumnName(field) + "=:value where " + sqlPrimaryKeyName + "=:id", map().add("value", value).add("id", idValue));
            }
        }
    }

    /**
     * Все поля модели аннотированные как @ResultsetField исключая @Id
     *
     * @param type
     * @return
     */
    private static List<Field> getUpdatableFields(Class<?> type) {
        return DTOUtils.getModelFields(type,
                field -> field.isAnnotationPresent(Column.class),
//                field -> !field.isAnnotationPresent(Id.class),
                field -> isFieldInTable(type, field));
    }

    public <T> T get(Class<T> type, Object id) {
        return get(type, getPrimaryKeyField(type), id);
    }

    private <T> T get(Class<T> type, Field field, Object value) {
        return getSingleResult(createSelectById(type, field), map("id", value));
    }

//    public <T> T getSingleResult(Select<T> query, Map<String, Object> params, RowMapper<T> mapper) {
//        return jdbcTemplate.queryForObject(
//                query.toString(), params, mapper);
//    }

    public <T> T getSingleResult(Select<T> query, Map<String, Object> params) {
        return jdbcTemplate.queryForObject(
                query.toString(), params, (resultSet, i) -> createEntity(resultSet, query.getType()));
    }

    private <T> void applyResult(Select<T> query, Map<String, Object> params, T instance) {
        jdbcTemplate.queryForObject(
                query.toString(), params, (resultSet, i) -> {
                    applyEntity(resultSet, instance);
                    return instance;
                });
    }

    public void remove(Object entity) {
        Class type = entity.getClass();
        Field primaryKeyField = requireNonNull(getPrimaryKeyField(type));
        String primaryKeyColumnName = getColumnName(primaryKeyField);
        String tableName = tableName(type);
        Object idValue = getValue(entity, primaryKeyField);

        jdbcTemplate.update("DELETE " + tableName + " WHERE " + primaryKeyColumnName + " = :id", map("id", idValue));
    }

    public void remove(Class type, Object idValue) {
        Field primaryKeyField = requireNonNull(getPrimaryKeyField(type));
        String primaryKeyColumnName = getColumnName(primaryKeyField);
        String tableName = tableName(type);

        jdbcTemplate.update("DELETE " + tableName + " WHERE " + primaryKeyColumnName + " = :id", map("id", idValue));
    }

    public void removeAll(Class type, Object idValues[]) {
        Field primaryKeyField = requireNonNull(getPrimaryKeyField(type));
        String primaryKeyColumnName = getColumnName(primaryKeyField);
        String tableName = tableName(type);

        jdbcTemplate.update("DELETE " + tableName + " WHERE " + primaryKeyColumnName + " in (:ids)", map("ids", idValues));
    }

//    public void remove(Object dto) {
//        Field primaryKeyField = getPrimaryKeyField(dto.getClass());
//        if (primaryKeyField == null) throw new RuntimeException("PrimaryKey not fount: " + dto.getClass());
//        remove(dto.getClass(), getValue(dto, primaryKeyField));
//    }
}
