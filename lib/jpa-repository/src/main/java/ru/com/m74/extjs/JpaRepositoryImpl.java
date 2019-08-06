package ru.com.m74.extjs;

import org.springframework.transaction.annotation.Transactional;
import ru.com.m74.extjs.dto.Request;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Map;

import static ru.com.m74.extjs.Utils.applyChanges;
import static ru.com.m74.extjs.Utils.getId;
import static ru.com.m74.extjs.Utils.set;

public class JpaRepositoryImpl<T> implements JpaRepository<T> {

    @PersistenceContext
    private EntityManager em;

    private Class<T> domain;

    public JpaRepositoryImpl(Class<T> domain) {
        this.domain = domain;
    }

    @Override
    public T get(Object id) {
        T entity = em.find(domain, id);
        if (entity == null) throw new RuntimeException("Entity not found: " + id);
        return entity;
    }

    @Override
    public Iterable<T> getAll(Request request) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> q = builder.createQuery(domain);
        q.select(q.from(domain));

        TypedQuery<T> tq = em.createQuery(q);

        if (request.isPaging()) {
            tq.setFirstResult(request.getStart());
            tq.setMaxResults(request.getLimit());
        }

        return tq.getResultList();
    }

    @Override
    public long count() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = builder.createQuery(Long.class);
        q.select(builder.count(q.from(domain)));

        return em.createQuery(q).getSingleResult();
    }

    @Override
    @Transactional
    public void deleteByIds(long ids[]) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<T> c = builder.createCriteriaDelete(domain);
        Root<T> root = c.from(domain);
        c.where(root.get("id").in(ids));
        em.createQuery(c).executeUpdate();
    }


    @Override
    @Transactional
    public T save(T entity) {
        set(entity, getId(entity.getClass()), null);
        em.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public T save(Object id, Map<String, Object> changes) {
        T user = get(id);
        applyChanges(user, changes);
        return em.merge(user);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        em.remove(entity);
    }

    @Override
    @Transactional
    public void deleteById(Object id) {
        delete(get(id));
    }
}
