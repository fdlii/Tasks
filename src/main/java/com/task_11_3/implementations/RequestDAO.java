package com.task_11_3.implementations;

import com.task_11_3.config.DBConfigurator;
import com.task_11_3.interfaces.IBookDAO;
import com.task_11_3.interfaces.IRequestDAO;
import com.task_3_4.Book;
import com.task_3_4.Request;
import com.task_8_2.annotations.Inject;
import com.task_8_2.annotations.PostConstruct;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO extends GenericDAO<Request, Integer> implements IRequestDAO {
    @Inject
    private IBookDAO bookDAO;
    @Inject
    private DBConfigurator dbConfigurator;

    public RequestDAO() {}

    @PostConstruct
    public void init() {
        this.tableName = dbConfigurator.getConfiguration().requestsTableName;
    }

    @Override
    protected Request mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        int bookID = resultSet.getInt(2);
        int count = resultSet.getInt(3);
        boolean isOpen = resultSet.getBoolean(4);

        Book requestBook = bookDAO.findById(bookID);
        return new Request(id, requestBook, count, isOpen);
    }

    @Override
    protected PreparedStatement setCreateParameters(Request entity) throws SQLException {
        String sql = "INSERT INTO " + tableName + " VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, entity.getId());
        preparedStatement.setObject(2, entity.getBook().getId());
        preparedStatement.setObject(3, entity.getCount());
        preparedStatement.setObject(4, entity.isOpen());
        return preparedStatement;
    }

    @Override
    public void createRequest(Request request) {
        try (PreparedStatement preparedStatement = setCreateParameters(request)) {
            preparedStatement.executeUpdate();
            System.out.println("Реквест добавлен.");
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void updateRequest(Request request) {
        String sql = "UPDATE " + tableName + " SET isopen = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, request.isOpen());
            preparedStatement.setObject(2, request.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Не удалось закрыть запрос на книгу.");
        }
    }

    @Override
    public List<Request> findRequestsByBookId(Book book) throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, book.getId());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            int count = resultSet.getInt(3);
            boolean isOpen = resultSet.getBoolean(4);
            Request request = new Request(id, book, count, isOpen);
            requests.add(request);
        }
        return requests;
    }
}
