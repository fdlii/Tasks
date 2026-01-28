package com.task_13.DAOs;

import com.task_13.HibernateConnector;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class GenericDAO<T, ID> implements IDAO<T, ID> {
    @Autowired
    HibernateConnector hibernateConnector;
    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T findById(ID id) {
        Session session = hibernateConnector.getCurrentSession();
        T entity = session.find(entityClass, id);
        return entity;
    }

    @Override
    public List<T> findAll() {
        Session session = hibernateConnector.getCurrentSession();
        List<T> entities = session
                .createSelectionQuery("FROM " + entityClass.getName(), entityClass)
                .getResultList();
        return entities;
    }

    @Override
    public void save(T entity) {
        Session session = hibernateConnector.getCurrentSession();
        session.persist(entity);
    }

    @Override
    public void update(T entity) {
        Session session = hibernateConnector.getCurrentSession();
        session.merge(entity);
    }

    @Override
    public void delete(T entity) {
        Session session = hibernateConnector.getCurrentSession();
        session.remove(entity);
    }
}
