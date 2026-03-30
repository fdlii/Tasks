package com.yourcompany.repositories;

import com.yourcompany.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("""
               SELECT COUNT(o.id)
               FROM OrderEntity o
               WHERE o.executionDate BETWEEN :fromDate AND :toDate
               AND o.orderStatus = 1
           """)
    Integer getCompletedOrdersCountForTimeSpan(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("""
                SELECT o
                FROM OrderEntity o
                WHERE o.executionDate BETWEEN :fromDate AND :toDate
                  AND o.orderStatus = 1
                """)
    List<OrderEntity> getCompletedOrdersForTimeSpan(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("""
                SELECT SUM(o.finalPrice)
                FROM OrderEntity o
                WHERE o.executionDate BETWEEN :fromDate AND :toDate
                  AND o.orderStatus = 1
                """)
    Double getEarnedFundsForTimeSpan(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}
