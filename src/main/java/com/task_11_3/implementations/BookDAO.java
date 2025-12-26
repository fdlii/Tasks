package com.task_11_3.implementations;

import com.task_11_3.interfaces.IBookDAO;
import com.task_3_4.Book;
import com.task_3_4.Request;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class BookDAO extends GenericDAO<Book, Integer> implements IBookDAO {
    private final RequestDAO requestDAOImplementation = new RequestDAO("requests");

    protected BookDAO(String tableName) {
        super(tableName);
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
        List<Request> bookRequests = requestDAOImplementation.findRequestsByBookId(book);
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
    public void createBook(Book book) {
        try (PreparedStatement preparedStatement = setCreateParameters(book)) {
            System.out.println("Добавлено книг: " + preparedStatement.executeUpdate());
        }
        catch (SQLException ex) {
            System.out.println("Не удалось добавить книгу в БД.");
        }
    }

    @Override
    public void deleteBook(String bookName) {
        String sql = "DELETE FROM " + tableName + " WHERE name = " + bookName;
        try (Statement statement = connection.createStatement()) {
            System.out.println("Удалено книг: " + statement.executeUpdate(sql));
        }
        catch (SQLException ex) {
            System.out.println("Не удалось удалить книгу из БД.");
        }
    }

    @Override
    public void addBookInStock(String bookName, int count) {
        Book book = findByName(bookName);
        String sql = "UPDATE " + tableName + "SET countinstock = ? WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, book.getCountInStock() + count);
            preparedStatement.setObject(2, bookName);
            System.out.println("Изменено книг: " + preparedStatement.executeUpdate());
        }
        catch (SQLException ex) {
            System.out.println("Не удалось добавить книгу на склад.");
        }
    }

    @Override
    public void debitBookFromStock(String bookName) {
        Book book = findByName(bookName);
        String sql = "UPDATE " + tableName + "SET countinstock = 0, instock = false WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, bookName);
            System.out.println("Изменено книг: " + preparedStatement.executeUpdate());
        }
        catch (SQLException ex) {
            System.out.println("Не удалось удалить книгу со склада.");
        }
    }

    @Override
    public List<Request> getBookRequests(String bookName) {
        try {
            Book book = findByName(bookName);
            return requestDAOImplementation.findRequestsByBookId(book);
        }
        catch (SQLException ex) {
            System.out.println("Не удалось получить запросы книги.");
            return null;
        }
    }

    private Book findByName(String bookName) {
        String sql = "SELECT * FROM " + tableName + " WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, bookName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        }
        catch (SQLException ex) {
            System.out.println("Не удалось создать запрос для нахождения сущности.");
        }
        return null;
    }
}
