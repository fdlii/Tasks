package com.task_11_3.implementations;

import com.task_11_3.interfaces.RequestDAO;
import com.task_3_4.Book;
import com.task_3_4.Request;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestDAOImplementation extends GenericDAOImplementation<Request, Integer> implements RequestDAO {
    private final BookDAOImplementation bookDAOImplementation = new BookDAOImplementation("books");

    public RequestDAOImplementation(String tableName) {
        super(tableName);
    }

    @Override
    protected Request mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        int bookID = resultSet.getInt(2);
        int count = resultSet.getInt(3);
        boolean isOpen = resultSet.getBoolean(4);

        Book requestBook = bookDAOImplementation.findById(bookID);
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
    protected PreparedStatement setUpdateParameters(Request entity) {
        return null;
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
