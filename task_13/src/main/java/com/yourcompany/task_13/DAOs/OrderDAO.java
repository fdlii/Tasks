package com.yourcompany.task_13.DAOs;

import com.yourcompany.task_13.entities.OrderEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDAO extends GenericDAO<OrderEntity, Long> {

    public OrderDAO() {
        super(OrderEntity.class);
    }

    public long getCompletedOrdersCountForTimeSpan(Date from, Date to) {
        LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Session session = hibernateConnector.getCurrentSession();
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

        return count != null ? count.intValue() : 0;
    }

    public List<OrderEntity> getCompletedOrdersForTimeSpan(Date from, Date to) {
        LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Session session = hibernateConnector.getCurrentSession();
        String hql = """
                SELECT COUNT(o.id)
                FROM OrderEntity o
                WHERE o.executionDate BETWEEN :fromDate AND :toDate
                  AND o.orderStatus = 1
                """;

        List<OrderEntity> entities = session.createQuery(hql, OrderEntity.class)
                .setParameter("fromDate", fromL)
                .setParameter("toDate", toL)
                .getResultList();
        return entities;
    }

    public double getEarnedFundsForTimeSpan(Date from, Date to) {
        LocalDateTime fromL = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toL = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Session session = hibernateConnector.getCurrentSession();
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

        return sum != null ? sum : 0;
    }
}
