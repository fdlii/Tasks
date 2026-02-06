package com.yourcompany.task_11_3.implementations;

import com.yourcompany.models.Book;
import com.yourcompany.models.Request;
import com.yourcompany.task_11_3.config.DBConfigurator;
import com.yourcompany.task_11_3.interfaces.IBookDAO;
import com.yourcompany.task_11_3.interfaces.IRequestDAO;
import com.yourcompany.task_8_1.Configurator;
import com.yourcompany.task_8_2.annotations.Inject;
import com.yourcompany.task_8_2.annotations.PostConstruct;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class BookDAO extends GenericDAO<Book, Integer> implements IBookDAO {
    @Inject
    private IRequestDAO requestDAO;
    @Inject
    private Configurator configurator;
    @Inject
    private DBConfigurator dbConfigurator;

    public BookDAO() {}

    @PostConstruct
    public void init() {
        this.tableName = dbConfigurator.getConfiguration().booksTableName;
    }

    @Override
    protected Book mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String author = resultSet.getString(3);
        String description = resultSet.getString(4);
        LocalDateTime published = resultSet.getTimestamp(5).toLocalDateTime();
        int countInStock = resultSet.getInt(7);
        double price = resultSet.getDouble(8);

        Book book = new Book(id, name, author, description, Date.from(published.atZone(ZoneId.systemDefault()).toInstant()), countInStock, price);
        List<Request> bookRequests = requestDAO.findRequestsByBookId(book);
        book.setRequests(bookRequests);

        return book;
    }

    @Override
    protected PreparedStatement setCreateParameters(Book entity) throws SQLException {
        String sql = "INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, entity.getId());
        preparedStatement.setObject(2, entity.getName());
        preparedStatement.setObject(3, entity.getAuthor());
        preparedStatement.setObject(4, entity.getDescription());
        preparedStatement.setObject(5, new Timestamp(entity.getPublished().getTime()));
        preparedStatement.setObject(6, entity.isInStock());
        preparedStatement.setObject(7, entity.getCountInStock());
        preparedStatement.setObject(8, entity.getPrice());
        return preparedStatement;
    }

    @Override
    public void createBook(Book book) throws SQLException {
        PreparedStatement preparedStatement = setCreateParameters(book);
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteBook(String bookName) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, bookName);
        statement.executeUpdate();
    }

    @Override
    public boolean addBookInStock(String bookName, int count) throws SQLException {
        try {
            connection.setAutoCommit(false);
            Book book = findByName(bookName);
            if (book == null) {
                return false;
            }
            book.setInStock(true);
            if (configurator.getConfiguration().canCompleteRequest) {
                for (Request r : book.getRequests()) {
                    if (r.getCount() <= book.getCountInStock() && r.isOpen()) {
                        r.setOpen(false);
                        requestDAO.updateRequest(r);
                        book.setCountInStock(book.getCountInStock() - r.getCount());
                    }
                }

                if (book.getCountInStock() == 0) {
                    book.setInStock(false);
                }
            }
            String sql = "UPDATE " + tableName + " SET instock = ?, countinstock = ? WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, book.isInStock());
            preparedStatement.setObject(2, book.getCountInStock() + count);
            preparedStatement.setObject(3, bookName);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        }
        catch (SQLException ex) {
            connection.rollback();
            throw new SQLException(ex);
        }
        return true;
    }

    @Override
    public boolean debitBookFromStock(String bookName) throws SQLException {
        Book book = findByName(bookName);
        if (book == null) {
            return false;
        }
        String sql = "UPDATE " + tableName + " SET countinstock = 0, instock = false WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, bookName);
        preparedStatement.executeUpdate();
        return true;
    }

    @Override
    public List<Request> getBookRequests(String bookName) throws SQLException {
        Book book = findByName(bookName);
        if (book == null) {
            throw new RuntimeException();
        }
        return requestDAO.findRequestsByBookId(book);
    }

    private Book findByName(String bookName) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, bookName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return mapResultSetToEntity(resultSet);
        }
        return null;
    }
}
