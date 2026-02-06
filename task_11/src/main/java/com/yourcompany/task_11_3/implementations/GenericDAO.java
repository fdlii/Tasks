package com.yourcompany.task_11_3.implementations;

import com.yourcompany.task_11_3.ConnectionManager;
import com.yourcompany.task_11_3.interfaces.IGenericDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T, ID> implements IGenericDAO<T, ID> {
    protected String tableName;
    protected Connection connection = ConnectionManager.getConnection();

    protected GenericDAO() {}

    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;
    protected abstract PreparedStatement setCreateParameters(T entity) throws SQLException;

    @Override
    public T findById(ID id) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return mapResultSetToEntity(resultSet);
        }
        return null;
    }

    @Override
    public List<T> findAll() throws SQLException {
        List<T> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            entities.add(mapResultSetToEntity(resultSet));
        }
        return entities;
    }
}
