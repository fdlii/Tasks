package com.task_11_3.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface IGenericDAO<T, ID> {
    T findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
}
