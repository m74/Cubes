package ru.com.m74.cubes.jdbc.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import ru.com.m74.cubes.jdbc.EntityManager;
import ru.com.m74.cubes.sql.base.Select;
import ru.com.m74.extjs.dto.Filter;
import ru.com.m74.extjs.dto.Request;

import java.util.List;
import java.util.Map;

import static ru.com.m74.cubes.common.MapUtils.map;
import static ru.com.m74.cubes.common.ObjectUtils.forEach;
import static ru.com.m74.cubes.jdbc.utils.EMUtils.filter;
import static ru.com.m74.cubes.jdbc.utils.EMUtils.sort;

public class ReadOnlyRepoImpl<T, I> implements ReadOnlyRepo<T, I> {
    protected final EntityManager em;

    private final Class<T> type;

    public ReadOnlyRepoImpl(EntityManager em, Class<T> type) {
        this.type = type;
        this.em = em;
    }

    protected Select<T> createAllQuery(Request request, Map<String, Object> params) {
        Select<T> q = em.select(type);
        filter(q, params, request.getFilter());
        forEach(request.getFilter(), filter -> handleFilter(q, params, filter));
        return q;
    }

    protected void handleFilter(Select<T> q, Map<String, Object> params, Filter filter){
        switch (filter.getProperty()) {
            case "query":
                handleQuery(q, params, (String) filter.getValue());
                break;
        }
    }

    protected void handleQuery(Select<T> q, Map<String, Object> params, String value) {

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
    public T get(I id) {
        try {
            return em.get(type, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
//            throw new RuntimeException("Объект " + type.getSimpleName() + "(" + tableName(type) + ") с идентификатором " + id + " не существует.", e);
        }
    }
}
