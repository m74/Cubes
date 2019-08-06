package ru.com.m74.cubes.jdbc.repository;

import ru.com.m74.cubes.jdbc.EntityManager;
import ru.com.m74.cubes.sql.base.Select;
import ru.com.m74.extjs.dto.Request;

import java.util.Map;

import static ru.com.m74.cubes.common.MapUtils.map;


public class AbstractRepoImpl<T> implements AbstractRepo<T> {

    protected final EntityManager em;

    private final Class<T> type;

    public AbstractRepoImpl(EntityManager em, Class<T> type) {
        this.type = type;
        this.em = em;
    }

    protected Select<T> createQuery(Map<String, Object> params) {
        return em.select(type);
    }

    @Override
    public Iterable<T> getAll(Request request) {
        Map<String, Object> params = map();
        Select<T> q = createQuery(params);
        if (request.isPaging()) {
            request.applyParams(params);
            q.setPagging(true);
        }
        return em.getResultList(q, params);
    }

    @Override
    public long count() {
        Map<String, Object> params = map();
        return em.count(createQuery(params), params);
    }

    @Override
    public T get(Object id) {
        return em.get(type, id);
    }

    @Override
    public T save(T entity) {
        em.save(entity);
        return entity;
    }

    @Override
    public T save(Object id, Map<String, Object> changes) {
        em.update(type, id, changes);
        return null;
    }

    @Override
    public void deleteById(Object id) {
        em.remove(type, id);
    }

//    @Override
//    public void deleteByIds(Object[] ids) {
//        em.remove(type, ids);
//    }
}
