package com.yourcompany.task_13.DAOs;

import java.util.List;

public interface IDAO<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T model);
    void update(T model);
    void delete(T model);
}
