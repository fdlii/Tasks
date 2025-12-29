package com.task_11_3.implementations;

import com.task_11_3.config.DBConfigurator;
import com.task_11_3.interfaces.IBookDAO;
import com.task_11_3.interfaces.IRequestDAO;
import com.task_3_4.Book;
import com.task_3_4.Request;
import com.task_8_1.Configurator;
import com.task_8_2.annotations.Inject;
import com.task_8_2.annotations.PostConstruct;

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
        String sql = "DELETE FROM " + tableName + " WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, bookName);
            System.out.println("Удалено книг: " + statement.executeUpdate());
        }
        catch (SQLException ex) {
            System.out.println("Не удалось удалить книгу из БД.");
        }
    }

    @Override
    public boolean addBookInStock(String bookName, int count) {
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
            System.out.println("Изменено книг: " + preparedStatement.executeUpdate());
            connection.commit();
            connection.setAutoCommit(true);
        }
        catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Не удалось добавить книгу на склад.");
        }
        return true;
    }

    @Override
    public boolean debitBookFromStock(String bookName) {
        Book book = findByName(bookName);
        if (book == null) {
            return false;
        }
        String sql = "UPDATE " + tableName + " SET countinstock = 0, instock = false WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, bookName);
            System.out.println("Изменено книг: " + preparedStatement.executeUpdate());
        }
        catch (SQLException ex) {
            System.out.println("Не удалось удалить книгу со склада.");
        }
        return true;
    }

    @Override
    public List<Request> getBookRequests(String bookName) {
        try {
            Book book = findByName(bookName);
            return requestDAO.findRequestsByBookId(book);
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
