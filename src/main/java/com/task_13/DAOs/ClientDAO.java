package com.task_13.DAOs;

import com.task_13.entities.ClientEntity;
import com.task_13.entities.OrderEntity;
import com.task_3_4.Client;
import com.task_3_4.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientDAO extends GenericDAO<Client, Long, ClientEntity> {
    @Autowired
    private OrderDAO orderDAO;

    public ClientDAO() {
        super(ClientEntity.class);
    }

    @Override
    protected Client mapFromEntityToModel(ClientEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Client(
                (int) entity.getId(),
                entity.getName(),
                entity.getAge()
        );
    }

    @Override
    protected ClientEntity mapFromModelToEntity(Client model, boolean ignoreId) {
        ClientEntity entity;
        if (ignoreId) {
            entity = new ClientEntity(
                    model.getName(),
                    model.getAge()
            );
        }
        else {
            entity = new ClientEntity(
                    model.getId(),
                    model.getName(),
                    model.getAge()
            );
        }
        List<OrderEntity> orderEntities = new ArrayList<>();
        for (Order order : model.getOrders()) {
            orderEntities.add(orderDAO.mapFromModelToEntity(order, false));
        }
        entity.setOrders(orderEntities);
        return entity;
    }
}
