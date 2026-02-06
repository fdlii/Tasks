package com.task_13.DAOs;

import com.task_13.HibernateConnector;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T, ID, M> implements IDAO<T, ID> {
    private final Class<M> entityClass;

    public GenericDAO(Class<M> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract T mapFromEntityToModel(M entity);
    protected abstract M mapFromModelToEntity(T model, boolean ignoreId);

    @Override
    public T findById(ID id) {
        T model = null;
        Transaction transaction = null;
        try(Session session = HibernateConnector.getSession()) {
            transaction = session.beginTransaction();
            M entity = session.find(entityClass, id);
            model = mapFromEntityToModel(entity);

            transaction.commit();
        }
        catch (HibernateException ex) {
            transaction.rollback();
            throw new HibernateException(ex);
        }
        return model;
    }

    @Override
    public List<T> findAll() {
        List<T> models = null;
        Transaction transaction = null;
        try (Session session = HibernateConnector.getSession()) {
            transaction = session.beginTransaction();
            List<M> entities = session
                    .createSelectionQuery("FROM " + entityClass.getName(), entityClass)
                    .getResultList();
            models = new ArrayList<>();
            for (M entity : entities) {
                models.add(mapFromEntityToModel(entity));
            }

            transaction.commit();
        }
        catch (HibernateException ex) {
            transaction.rollback();
            throw new HibernateException(ex);
        }
        return models;
    }

    @Override
    public void save(T model) {
        Transaction transaction = null;
        try (Session session = HibernateConnector.getSession()) {
            transaction = session.beginTransaction();
            M entity = mapFromModelToEntity(model, true);
            session.persist(entity);

            transaction.commit();
        }
        catch (HibernateException ex) {
            transaction.rollback();
            throw new HibernateException(ex);
        }
    }

    @Override
    public void update(T model) {
        Transaction transaction = null;
        try (Session session = HibernateConnector.getSession()) {
            transaction = session.beginTransaction();
            M entity = mapFromModelToEntity(model, false);
            session.merge(entity);

            transaction.commit();
        }
        catch (HibernateException ex) {
            transaction.rollback();
            throw new HibernateException(ex);
        }
    }

    @Override
    public void delete(T model) {
        Transaction transaction = null;
        try (Session session = HibernateConnector.getSession()) {
            transaction = session.beginTransaction();
            M entity = mapFromModelToEntity(model, false);
            session.remove(entity);

            transaction.commit();
        }
        catch (HibernateException ex) {
            transaction.rollback();
            throw new HibernateException(ex);
        }
    }
}
