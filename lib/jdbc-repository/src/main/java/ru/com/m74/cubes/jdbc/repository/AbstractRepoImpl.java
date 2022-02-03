package ru.com.m74.cubes.jdbc.repository;

import ru.com.m74.cubes.jdbc.EntityManager;
import ru.com.m74.cubes.jdbc.utils.DTOUtils;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static ru.com.m74.cubes.jdbc.utils.DTOUtils.getPrimaryKeyField;
import static ru.com.m74.cubes.jdbc.utils.DTOUtils.getValue;


public abstract class AbstractRepoImpl<T, I> extends ReadOnlyRepoImpl<T, I> implements AbstractRepo<T, I> {
    protected final EntityManager em;

    private final Class<T> type;

    public AbstractRepoImpl(EntityManager em, Class<T> type) {
        super(em, type);
        this.type = type;
        this.em = em;
    }

    @Override
    public T save(T entity) {
        em.save(entity);
        return getEntity(entity);
    }

    @Override
    public T persist(T entity) {
        em.persist(entity);
        return getEntity(entity);
    }

    @SuppressWarnings({"unchecked"})
    public T getEntity(T entity) {
        Field idField = requireNonNull(getPrimaryKeyField(type), "Требуется аннотация @Id");
        I id = (I) getValue(entity, idField);
        return get(id);
    }

    @Override
    public T save(I id, Map<String, Object> changes) {
        em.update(type, id, changes);
        return get(id);
    }

    @Override
    public void deleteById(I id) {
        em.remove(type, id);
    }

    @Override
    public void deleteByIds(I[] ids) {
        em.removeAll(type, ids);
    }

    @Override
    public void convert(Map<String, Object> changes) {
        for (String key : changes.keySet()) {
            Field f = DTOUtils.findField(type, key);
            if (f != null) {
                if (changes.get(key) instanceof Map) {
                    changes.replace(key, ((Map<String, Object>) changes.get(key)).get("id"));
                }
            }
        }
    }

    public Class<T> getType() {
        return type;
    }


    @SuppressWarnings({"unchecked"})
    public static Map<String, Object> asMap(Object obj) {
        return obj != null ? (Map<String, Object>) obj : Collections.emptyMap();
    }

    public static void replace(Map<String, Object> changes, String key, Handler handler) {
        if (changes.containsKey(key)) {
            Object val = changes.get(key);
            changes.remove(key);
            Map.Entry<String, Object> entry = handler.handle(val);
            changes.put(entry.getKey(), entry.getValue());
        }
    }

    public static <K, V> Map.Entry<K, V> entry(K key, V val) {
        return new AbstractMap.SimpleEntry<>(key, val);
    }

    public interface Handler {
        Map.Entry<String, Object> handle(Object value);
    }
}
