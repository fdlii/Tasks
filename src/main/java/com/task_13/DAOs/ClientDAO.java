package com.task_13.DAOs;

import com.task_13.entities.ClientEntity;
import com.task_13.entities.OrderEntity;
import com.task_3_4.Client;
import com.task_3_4.Order;

import java.util.ArrayList;
import java.util.List;

public class ClientDAO extends GenericDAO<Client, Long, ClientEntity> {
    private OrderDAO orderDAO = new OrderDAO();

    public ClientDAO() {
        super(ClientEntity.class);
    }

    @Override
    protected Client mapFromEntityToModel(ClientEntity entity) {
        Client model = new Client(
                (int) entity.getId(),
                entity.getName(),
                entity.getAge()
        );
        List<Order> orders = new ArrayList<>();
        for (OrderEntity orderEntity : entity.getOrders()) {
            orders.add(orderDAO.mapFromEntityToModel(orderEntity));
        }
        model.setOrders(orders);
        return model;
    }

    @Override
    protected ClientEntity mapFromModelToEntity(Client model) {
        ClientEntity entity = new ClientEntity(
                model.getId(),
                model.getName(),
                model.getAge()
        );
        List<OrderEntity> orderEntities = new ArrayList<>();
        for (Order order : model.getOrders()) {
            orderEntities.add(orderDAO.mapFromModelToEntity(order));
        }
        entity.setOrders(orderEntities);
        return entity;
    }
}
