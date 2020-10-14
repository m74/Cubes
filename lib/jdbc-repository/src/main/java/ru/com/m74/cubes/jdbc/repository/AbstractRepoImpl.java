package ru.com.m74.cubes.jdbc.repository;

import ru.com.m74.cubes.jdbc.EntityManager;
import ru.com.m74.cubes.jdbc.utils.DTOUtils;

import java.lang.reflect.Field;
import java.util.Map;


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
        em.refresh(entity);
        return entity;
    }

    @Override
    public T persist(T entity) {
        em.persist(entity);
        em.refresh(entity);
        return entity;
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
}
