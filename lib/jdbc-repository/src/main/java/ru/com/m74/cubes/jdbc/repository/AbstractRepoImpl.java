package ru.com.m74.cubes.jdbc.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import ru.com.m74.cubes.jdbc.EntityManager;
import ru.com.m74.cubes.sql.base.Select;
import ru.com.m74.extjs.dto.Request;

import java.util.List;
import java.util.Map;

import static ru.com.m74.cubes.common.MapUtils.map;
import static ru.com.m74.cubes.jdbc.utils.EMUtils.sort;


public class AbstractRepoImpl<T> implements AbstractRepo<T> {

    protected final EntityManager em;

    private final Class<T> type;

    public AbstractRepoImpl(EntityManager em, Class<T> type) {
        this.type = type;
        this.em = em;
    }

    protected Select<T> createAllQuery(Request request, Map<String, Object> params) {
        return em.select(type);
    }

    @Override
    public List<T> getAll(Request request, Map<String, Object> params) {
        Select<T> q = createAllQuery(request, params);
        if (request.isPaging()) {
            q.setPagging(true);
        }
        request.applyParams(params);
        sort(q, request.getSort());
        return em.getResultList(q, params);
    }

    @Override
    public List<T> getAll(Request request) {
        return getAll(request, map());
    }

    @Override
    public long count(Request request, Map<String, Object> params) {
        request.applyParams(params);
        return em.count(createAllQuery(request, params), params);
    }

    @Override
    public T get(Object id) {
        try {
            return em.get(type, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public T save(T entity) {
        em.save(entity);
        return entity;
    }

    @Override
    public T persist(T entity) {
        return em.persist(entity);
    }

    @Override
    public T save(Object id, Map<String, Object> changes) {
        em.update(type, id, changes);
        return get(id);
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
