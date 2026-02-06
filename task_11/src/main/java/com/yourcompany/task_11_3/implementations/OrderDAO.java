package com.yourcompany.task_11_3.implementations;

import com.yourcompany.models.Book;
import com.yourcompany.models.Client;
import com.yourcompany.models.Order;
import com.yourcompany.models.OrderStatus;
import com.yourcompany.task_11_3.config.DBConfigurator;
import com.yourcompany.task_11_3.interfaces.IBookDAO;
import com.yourcompany.task_11_3.interfaces.IClientDAO;
import com.yourcompany.task_11_3.interfaces.IOrderDAO;
import com.yourcompany.task_8_2.annotations.Inject;
import com.yourcompany.task_8_2.annotations.PostConstruct;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAO extends GenericDAO<Order, Integer> implements IOrderDAO {
    @Inject
    private DBConfigurator dbConfigurator;
    @Inject
    private IBookDAO bookDAO;
    @Inject
    private IClientDAO clientDAO;
    private String subTable;

    public OrderDAO() {}

    @PostConstruct
    public void init() {
        this.tableName = dbConfigurator.getConfiguration().ordersTableName;
        this.subTable = dbConfigurator.getConfiguration().MMsTableName;
    }

    @Override
    protected Order mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        int clientID = resultSet.getInt(2);
        double discount = resultSet.getDouble(3);
        double finalPrice = resultSet.getDouble(4);
        Date executionDate = new Date(resultSet.getTimestamp(5).getTime());
        OrderStatus orderStatus = OrderStatus.values()[resultSet.getInt(6)];

        List<Integer> bookIDs = new ArrayList<>();
        String subSQL = "SELECT bookid FROM " + subTable + " WHERE orderid = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(subSQL);
        preparedStatement.setObject(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            bookIDs.add(rs.getInt(1));
        }

        List<Book> books = new ArrayList<>();
        for (int bookId : bookIDs) {
            books.add(bookDAO.findById(bookId));
        }

        Client client = clientDAO.findById(clientID);
        Order order = new Order(id, client, discount, finalPrice, executionDate, orderStatus);
        order.setBooks(books);
        return order;
    }

    @Override
    protected PreparedStatement setCreateParameters(Order entity) throws SQLException {
        String sql = "INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, entity.getId());
        preparedStatement.setObject(2, entity.getClient().getId());
        preparedStatement.setObject(3, entity.getDiscount());
        preparedStatement.setObject(4, entity.getFinalPrice());
        preparedStatement.setObject(5, new Timestamp(entity.getExecutionDate().getTime()));
        preparedStatement.setObject(6, entity.getOrderStatus().ordinal());
        return preparedStatement;
    }

    @Override
    public List<Book> getStaledBooks(Timestamp currentTime) throws SQLException {
        String sql = """
            SELECT DISTINCT books.id FROM orders
            JOIN orders_books ON orders.id = orders_books.orderid
            JOIN books ON orders_books.bookid = books.id
            WHERE orders.executiondate < ( ? - INTERVAL '? MONTH' )
            """;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, currentTime);
        preparedStatement.setObject(2, 6);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Integer> bookIds = new ArrayList<>();
        while (resultSet.next()) {
            bookIds.add(resultSet.getInt(1));
        }

        List<Book> books = new ArrayList<>();
        for (int id : bookIds) {
            books.add(bookDAO.findById(id));
        }

        return books;
    }

    @Override
    public double getEarnedFundsForTimeSpan(Date from, Date to) throws SQLException {
        Timestamp fromT = new Timestamp(from.getTime());
        Timestamp toT = new Timestamp(to.getTime());

        String sql = "SELECT * FROM " + tableName + " WHERE executiondate BETWEEN ? AND ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, fromT);
        preparedStatement.setObject(2, toT);
        ResultSet resultSet = preparedStatement.executeQuery();

        double earnedFunds = 0;
        while (resultSet.next()) {
            earnedFunds += resultSet.getDouble(4);
        }
        return earnedFunds;
    }

    @Override
    public int getCompletedOrdersCountForTimeSpan(Date from, Date to) throws SQLException {
        Timestamp fromT = new Timestamp(from.getTime());
        Timestamp toT = new Timestamp(to.getTime());

        String sql = "SELECT * FROM " + tableName + " WHERE (executiondate BETWEEN ? AND ?) AND orderstatus = 1";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, fromT);
        preparedStatement.setObject(2, toT);
        ResultSet resultSet = preparedStatement.executeQuery();

        int count = 0;
        while (resultSet.next()) {
            count++;
        }
        return count;
    }

    @Override
    public List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) throws SQLException {
        Timestamp fromT = new Timestamp(from.getTime());
        Timestamp toT = new Timestamp(to.getTime());

        String sql = "SELECT * FROM " + tableName + " WHERE executiondate BETWEEN ? AND ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, fromT);
        preparedStatement.setObject(2, toT);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            orders.add(mapResultSetToEntity(resultSet));
        }
        return orders;
    }

    @Override
    public void createOrder(Order order) throws SQLException {
        try (PreparedStatement preparedStatement = setCreateParameters(order)) {
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            String sql = "INSERT INTO " + subTable + " VALUES (?, ?)";
            for (Book book : order.getBooks()) {
                PreparedStatement subStatement = connection.prepareStatement(sql);
                subStatement.setObject(1, order.getId());
                subStatement.setObject(2, book.getId());
                subStatement.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException(e);
        }
    }

    @Override
    public void updateOrder(Order order) throws SQLException {
        String sql = "UPDATE " + tableName + " SET clientid = ?, discount = ?, finalprice = ?, executiondate = ?, orderstatus = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, order.getClient().getId());
        preparedStatement.setObject(2, order.getDiscount());
        preparedStatement.setObject(3, order.getFinalPrice());
        preparedStatement.setObject(4, new Timestamp(order.getExecutionDate().getTime()));
        preparedStatement.setObject(5, order.getOrderStatus().ordinal());
        preparedStatement.setObject(6, order.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Order> findOrdersByClientId(Client client) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE clientid = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, client.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            double discount = resultSet.getDouble(3);
            double finalPrice = resultSet.getDouble(4);
            Date executionDate = new Date(resultSet.getTimestamp(5).getTime());
            OrderStatus orderStatus = OrderStatus.values()[resultSet.getInt(6)];
            orders.add(new Order(id, client, discount, finalPrice, executionDate, orderStatus));
        }
        return orders;
    }
}