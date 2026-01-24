package com.task_13.DAOs;

import com.task_13.HibernateConnector;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public abstract class GenericDAO<T, ID, M> implements IDAO<T, ID> {

    private final Class<M> entityClass;

    @Autowired
    protected HibernateConnector hibernateConnector;

    public GenericDAO(Class<M> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract T mapFromEntityToModel(M entity);
    protected abstract M mapFromModelToEntity(T model, boolean ignoreId);

    @Override
    @Transactional(readOnly = true)
    public T findById(ID id) {
        Session session = hibernateConnector.getSession();
        M entity = session.find(entityClass, id);
        return entity != null ? mapFromEntityToModel(entity) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        Session session = hibernateConnector.getSession();

        List<M> entities = session
                .createSelectionQuery("FROM " + entityClass.getName(), entityClass)
                .getResultList();

        return entities.stream()
                .map(this::mapFromEntityToModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(T model) {
        Session session = hibernateConnector.getSession();
        M entity = mapFromModelToEntity(model, true);
        session.persist(entity);
    }

    @Override
    @Transactional
    public void update(T model) {
        Session session = hibernateConnector.getSession();
        M entity = mapFromModelToEntity(model, false);
        session.merge(entity);
    }

    @Override
    @Transactional
    public void delete(T model) {
        Session session = hibernateConnector.getSession();
        M entity = mapFromModelToEntity(model, false);
        session.remove(entity);
    }
}