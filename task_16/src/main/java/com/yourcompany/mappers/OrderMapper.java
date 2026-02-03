package com.yourcompany.mappers;

import com.yourcompany.DTO.OrderDTO;
import com.yourcompany.models.Order;
import com.yourcompany.task_13.entities.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderMapper implements IMapper<OrderDTO, Order, OrderEntity> {
    @Autowired
    ClientMapper clientMapper;

    @Override
    public Order toModel(OrderEntity entity) {
        return new Order(
                entity.getId(),
                clientMapper.toModel(entity.getClient()),
                entity.getDiscount(),
                entity.getFinalPrice(),
                Date.from(entity.getExecutionDate().atZone(ZoneId.systemDefault()).toInstant()),
                entity.getOrderStatus()
        );
    }

    @Override
    public OrderEntity toEntity(Order model, boolean ignoreId) {
        if (ignoreId) {
            return new OrderEntity(
                    clientMapper.toEntity(model.getClient(), false),
                    model.getDiscount(),
                    model.getFinalPrice(),
                    model.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    model.getOrderStatus()
            );
        }
        return new OrderEntity(
                model.getId(),
                clientMapper.toEntity(model.getClient(), false),
                model.getDiscount(),
                model.getFinalPrice(),
                model.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                model.getOrderStatus()
        );
    }

    @Override
    public List<Order> toModelsList(List<OrderEntity> entities) {
        List<Order> orders = new ArrayList<>();
        for (OrderEntity entity : entities) {
            orders.add(toModel(entity));
        }
        return orders;
    }

    @Override
    public Order fromDTOtoModel(OrderDTO DTO) {
        return new Order(
                DTO.getId(),
                clientMapper.fromDTOtoModel(DTO.getClient()),
                DTO.getDiscount(),
                DTO.getFinalPrice(),
                DTO.getExecutionDate(),
                DTO.getOrderStatus()
        );
    }

    @Override
    public OrderDTO fromModelToDTO(Order model) {
        return new OrderDTO(
                model.getId(),
                clientMapper.fromModelToDTO(model.getClient()),
                model.getDiscount(),
                model.getFinalPrice(),
                model.getExecutionDate(),
                model.getOrderStatus()
        );
    }
}
