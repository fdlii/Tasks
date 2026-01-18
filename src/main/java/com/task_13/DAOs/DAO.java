package com.task_13.DAOs;

import java.util.List;

public interface DAO<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    T update(T entity);
    void delete(T entity);
}
