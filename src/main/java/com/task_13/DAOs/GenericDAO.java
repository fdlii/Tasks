package com.task_13.DAOs;

import com.task_13.HibernateConnector;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T, ID, M> implements IDAO<T, ID> {
    private final Class<M> entityClass;

    public GenericDAO(Class<M> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract T mapFromEntityToModel(M entity);
    protected abstract M mapFromModelToEntity(T model);

    @Override
    public T findById(ID id) {
        Session session = HibernateConnector.getSession();
        M entity = session.find(entityClass, id);
        session.close();
        return mapFromEntityToModel(entity);
    }

    @Override
    public List<T> findAll() {
        Session session = HibernateConnector.getSession();
        List<M> entities = session
                .createSelectionQuery("FROM " + entityClass.getName(), entityClass)
                .getResultList();
        List<T> models = new ArrayList<>();
        for (M entity : entities) {
            models.add(mapFromEntityToModel(entity));
        }
        session.close();
        return models;
    }

    @Override
    public void save(T model) {
        Session session = HibernateConnector.getSession();
        M entity = mapFromModelToEntity(model);
        session.persist(entity);
        session.close();
    }

    @Override
    public void update(T model) {
        Session session = HibernateConnector.getSession();
        M entity = mapFromModelToEntity(model);
        session.merge(entity);
        session.close();
    }

    @Override
    public void delete(T model) {
        Session session = HibernateConnector.getSession();
        M entity = mapFromModelToEntity(model);
        session.remove(entity);
        session.close();
    }
}
