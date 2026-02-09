package com.yourcompany.mappers;

import com.yourcompany.DTO.OrderDTO;
import com.yourcompany.entities.OrderEntity;
import com.yourcompany.models.Book;
import com.yourcompany.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderMapper implements IMapper<OrderDTO, Order, OrderEntity> {
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    BookMapper bookMapper;

    @Override
    public Order toModel(OrderEntity entity) {
        Order order = new Order(
                entity.getId(),
                clientMapper.toModel(entity.getClient()),
                entity.getDiscount(),
                entity.getFinalPrice(),
                Date.from(entity.getExecutionDate().atZone(ZoneId.systemDefault()).toInstant()),
                entity.getOrderStatus()
        );
        order.setBooks(bookMapper.toModelsList(entity.getBooks()));
        return order;
    }

    @Override
    public OrderEntity toEntity(Order model, boolean ignoreId) {
        if (ignoreId) {
            OrderEntity orderEntity = new OrderEntity(
                    clientMapper.toEntity(model.getClient(), false),
                    model.getDiscount(),
                    model.getFinalPrice(),
                    model.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    model.getOrderStatus()
            );
            orderEntity.setBooks(bookMapper.toEntityList(model.getBooks()));
            return orderEntity;
        }
        OrderEntity orderEntity = new OrderEntity(
                model.getId(),
                clientMapper.toEntity(model.getClient(), false),
                model.getDiscount(),
                model.getFinalPrice(),
                model.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                model.getOrderStatus()
        );
        orderEntity.setBooks(bookMapper.toEntityList(model.getBooks()));
        return orderEntity;
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
    public OrderDTO fromModelToDTO(Order model) {
        OrderDTO orderDTO = new OrderDTO(
                model.getId(),
                model.getClient().getName(),
                model.getDiscount(),
                model.getFinalPrice(),
                model.getExecutionDate(),
                model.getOrderStatus(),
                new ArrayList<>()
        );
        for (Book book : model.getBooks()) {
            orderDTO.bookNames.add(book.getName());
        }
        return orderDTO;
    }

    @Override
    public List<OrderDTO> toDTOList(List<Order> models) {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (Order order : models) {
            orderDTOList.add(fromModelToDTO(order));
        }
        return orderDTOList;
    }
}
