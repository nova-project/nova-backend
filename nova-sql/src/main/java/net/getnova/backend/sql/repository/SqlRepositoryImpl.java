package net.getnova.backend.sql.repository;

import net.getnova.backend.sql.SqlService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Stream;

public class SqlRepositoryImpl<T, K> implements SqlRepository<T, K> {

    private final Class<T> entityType;

    @Inject
    private SqlService sqlService;

    public SqlRepositoryImpl(final Class<T> entityType) {
        this.entityType = entityType;
    }

    private Query<T> selectAll(final Session session) {
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(this.entityType);
        criteriaQuery.select(criteriaQuery.from(this.entityType));
        return session.createQuery(criteriaQuery);
    }

    @Override
    public final List<T> list() {
        try (Session session = this.sqlService.openSession()) {
            return this.selectAll(session).getResultList();
        }
    }

    @Override
    public final Stream<T> stream() {
        try (Session session = this.sqlService.openSession()) {
            return this.selectAll(session).getResultStream();
        }
    }

    @Override
    public final T find(final K key) {
        try (Session session = this.sqlService.openSession()) {
            return session.find(this.entityType, key);
        }
    }

    @Override
    public final T save(final T entity) {
        try (Session session = this.sqlService.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return entity;
        }
    }

    @Override
    public final T update(final T entity) {
        try (Session session = this.sqlService.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            return entity;
        }
    }

    @Override
    public final T saveOrUpdate(final T entity) {
        try (Session session = this.sqlService.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();
            return entity;
        }
    }

    /**
     * Deletes the entity with the specified primary key.
     *
     * @param key the primary key of the entity, which should be deleted
     * @return if the entity with the primary key was found and deleted
     */
    @Override
    public boolean delete(final K key) {
        try (Session session = this.sqlService.openSession()) {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(this.entityType);
            criteriaDelete.where(criteriaBuilder.equal(criteriaDelete.from(this.entityType).get("id"), key));

            final Transaction transaction = session.beginTransaction();
            final int count = session.createQuery(criteriaDelete).executeUpdate();
            transaction.commit();
            return count != 0;
        }
    }
}
