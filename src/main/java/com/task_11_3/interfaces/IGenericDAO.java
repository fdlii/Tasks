package com.task_11_3.interfaces;

import java.util.List;

public interface IGenericDAO<T, ID> {
    T findById(ID id);
    List<T> findAll();
}
