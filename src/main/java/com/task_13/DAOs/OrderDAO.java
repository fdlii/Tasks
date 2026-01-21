package com.task_13.DAOs;

import com.task_13.HibernateConnector;
import com.task_13.entities.BookEntity;
import com.task_13.entities.OrderEntity;
import com.task_3_4.Book;
import com.task_3_4.Order;
import com.task_8_2.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAO extends GenericDAO<Order, Long, OrderEntity> {
    @Inject
    ClientDAO clientDAO;
    @Inject
    BookDAO bookDAO;

    public OrderDAO() {
        super(OrderEntity.class);
    }

    @Override
    protected Order mapFromEntityToModel(OrderEntity entity) {
        return new Order(
                (int) entity.getId(),
                clientDAO.mapFromEntityToModel(entity.getClient()),
                entity.getDiscount(),
                entity.getFinalPrice(),
                Date.from(entity.getExecutionDate().atZone(ZoneId.systemDefault()).toInstant()),
                entity.getOrderStatus()
        );
    }

    @Override
    protected OrderEntity mapFromModelToEntity(Order model, boolean ignoreId) {
        OrderEntity entity;
        if (ignoreId) {
            entity = new OrderEntity(
                    clientDAO.mapFromModelToEntity(model.getClient(), false),
                    model.getDiscount(),
                    model.getFinalPrice(),
                    model.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    model.getOrderStatus()
            );
        }
        else {
            entity = new OrderEntity(
                    model.getId(),
                    clientDAO.mapFromModelToEntity(model.getClient(), false),
                    model.getDiscount(),
                    model.getFinalPrice(),
                    model.getExecutionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    model.getOrderStatus()
            );
        }
        List<BookEntity> bookEntities = new ArrayList<>();
        for (Book book : model.getBooks()) {
            bookEntities.add(bookDAO.mapFromModelToEntity(book, false));
        }
        entity.setBooks(bookEntities);
        return entity;
    }

    public long getCompletedOrdersCountForTimeSpan(Date from, Date to) {
        LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        try (Session session = HibernateConnector.getSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = """
                    SELECT COUNT(o.id)
                    FROM OrderEntity o
                    WHERE o.executionDate BETWEEN :fromDate AND :toDate
                      AND o.orderStatus = 1
                    """;

            Long count = session.createQuery(hql, Long.class)
                    .setParameter("fromDate", fromL)
                    .setParameter("toDate", toL)
                    .getSingleResult();

            transaction.commit();
            session.close();
            return count != null ? count.intValue() : 0;
        }
    }

    public List<Order> getCompletedOrdersForTimeSpan(Date from, Date to) {
        LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        try (Session session = HibernateConnector.getSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = """
                    SELECT COUNT(o.id)
                    FROM OrderEntity o
                    WHERE o.executionDate BETWEEN :fromDate AND :toDate
                      AND o.orderStatus = 1
                    """;

            List<OrderEntity> orderEntities = session.createQuery(hql, OrderEntity.class)
                    .setParameter("fromDate", fromL)
                    .setParameter("toDate", toL)
                    .getResultList();

            List<Order> orders = new ArrayList<>();
            for (OrderEntity orderEntity : orderEntities) {
                orders.add(mapFromEntityToModel(orderEntity));
            }

            transaction.commit();
            session.close();
            return orders;
        }
    }

    public double getEarnedFundsForTimeSpan(Date from, Date to) {
        LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        try (Session session = HibernateConnector.getSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = """
                    SELECT SUM(o.finalPrice)
                    FROM OrderEntity o
                    WHERE o.executionDate BETWEEN :fromDate AND :toDate
                      AND o.orderStatus = 1
                    """;

            Double sum = session.createQuery(hql, Double.class)
                    .setParameter("fromDate", fromL)
                    .setParameter("toDate", toL)
                    .getSingleResult();

            transaction.commit();
            session.close();
            return sum != null ? sum : 0;
        }
    }

    public List<Book> getStaledBooks(LocalDateTime now) {
        try (Session session = HibernateConnector.getSession()) {
            Transaction transaction = session.beginTransaction();

            LocalDateTime threshold = now.minusMonths(6);
            String sql = """
                SELECT b.*
                FROM books b
                LEFT JOIN orders_books ob ON b.id = ob.bookid
                LEFT JOIN orders o ON ob.orderid = o.id
                       AND o.executiondate >= ?
                WHERE o.id IS NULL
            """;

            List<BookEntity> entities = session.createNativeQuery(sql, BookEntity.class)
                    .setParameter(1, threshold)
                    .getResultList();

            List<Book> books = new ArrayList<>();
            for (BookEntity bookEntity : entities) {
                books.add(bookDAO.mapFromEntityToModel(bookEntity));
            }

            transaction.commit();
            session.close();
            return books;
        }
    }
}
