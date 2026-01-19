package com.task_11_3.implementations;

import com.task_11_3.config.DBConfigurator;
import com.task_11_3.interfaces.IClientDAO;
import com.task_11_3.interfaces.IOrderDAO;
import com.task_3_4.Client;
import com.task_3_4.Order;
import com.task_8_2.annotations.Inject;
import com.task_8_2.annotations.PostConstruct;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClientDAO extends GenericDAO<Client, Integer> implements IClientDAO {
    @Inject
    private IOrderDAO orderDAO;
    @Inject
    private DBConfigurator dbConfigurator;

    public ClientDAO() {}

    @PostConstruct
    public void init() {
        this.tableName = dbConfigurator.getConfiguration().clientsTableName;
    }

    @Override
    protected Client mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        int age = resultSet.getInt(3);
        Client client = new Client(id, name, age);

        List<Order> orders = orderDAO.findOrdersByClientId(client);
        client.setOrders(orders);
        return client;
    }

    @Override
    protected PreparedStatement setCreateParameters(Client entity) throws SQLException {
        String sql = "INSERT INTO " + tableName + " VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, entity.getId());
        preparedStatement.setObject(2, entity.getName());
        preparedStatement.setObject(3, entity.getAge());
        return preparedStatement;
    }

    @Override
    public void createClient(Client client) {
        try (PreparedStatement preparedStatement = setCreateParameters(client)) {
            System.out.println("Добавлено клиентов: " + preparedStatement.executeUpdate());
        } catch (SQLException e) {
            System.out.println("Не удалось добавить клиента в БД.");
        }
    }
}
