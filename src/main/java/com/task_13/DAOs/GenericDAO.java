package com.task_13.DAOs;

import com.task_13.HibernateConnector;
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
        Session session = HibernateConnector.getSession();
        Transaction transaction = session.beginTransaction();
        M entity = session.find(entityClass, id);
        T model = mapFromEntityToModel(entity);

        transaction.commit();
        session.close();
        return model;
    }

    @Override
    public List<T> findAll() {
        Session session = HibernateConnector.getSession();
        Transaction transaction = session.beginTransaction();
        List<M> entities = session
                .createSelectionQuery("FROM " + entityClass.getName(), entityClass)
                .getResultList();
        List<T> models = new ArrayList<>();
        for (M entity : entities) {
            models.add(mapFromEntityToModel(entity));
        }

        transaction.commit();
        session.close();
        return models;
    }

    @Override
    public void save(T model) {
        Session session = HibernateConnector.getSession();
        Transaction transaction = session.beginTransaction();
        M entity = mapFromModelToEntity(model, true);
        session.persist(entity);

        transaction.commit();
        session.close();
    }

    @Override
    public void update(T model) {
        Session session = HibernateConnector.getSession();
        Transaction transaction = session.beginTransaction();
        M entity = mapFromModelToEntity(model, false);
        session.merge(entity);

        transaction.commit();
        session.close();
    }

    @Override
    public void delete(T model) {
        Session session = HibernateConnector.getSession();
        Transaction transaction = session.beginTransaction();
        M entity = mapFromModelToEntity(model, false);
        session.remove(entity);

        transaction.commit();
        session.close();
    }
}
