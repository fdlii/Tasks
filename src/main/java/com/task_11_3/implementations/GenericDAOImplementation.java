package com.task_11_3.implementations;

import com.task_11_3.ConnectionManager;
import com.task_11_3.interfaces.GenericDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAOImplementation<T, ID> implements GenericDAO<T, ID> {
    protected final String tableName;
    protected Connection connection = ConnectionManager.getConnection();

    protected GenericDAOImplementation(String tableName) {
        this.tableName = tableName;
    }

    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;
    protected abstract PreparedStatement setCreateParameters(T entity) throws SQLException;
    protected abstract PreparedStatement setUpdateParameters(T entity);

    @Override
    public T findById(ID id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        }
        catch (SQLException ex) {
            System.out.println("Не удалось подготовить запрос для нахождения сущности.");
        }
        return null;
    }

    @Override
    public List<T> findAll() {
        List<T> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
            };
        }
        catch (SQLException ex) {
            System.out.println("Не удалось создать запрос для нахождения сущностей.");
        }
        return entities;
    }
}
