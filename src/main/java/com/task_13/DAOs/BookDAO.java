package com.task_13.DAOs;

import com.task_13.entities.BookEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BookDAO extends GenericDAO<BookEntity, Long> {

    public BookDAO() {
        super(BookEntity.class);
    }

    public BookEntity findByName(String bookName) {
        BookEntity entity;
        Session session = hibernateConnector.getCurrentSession();
        String hql = """
                SELECT b
                FROM BookEntity b
                WHERE b.name = :bookname
                """;

        entity = session.createQuery(hql, BookEntity.class)
                .setParameter("bookname", bookName)
                .getSingleResult();

        return entity;
    }

    public List<BookEntity> getStaledBooks(LocalDateTime now, int monthsCount) {
        List<BookEntity> entities;
        Session session = hibernateConnector.getCurrentSession();
        LocalDateTime threshold = now.minusMonths(monthsCount);

        String sql = """
            SELECT b.*
            FROM books b
            LEFT JOIN orders_books ob ON b.id = ob.bookid
            LEFT JOIN orders o ON ob.orderid = o.id
                   AND o.executiondate >= ?
            WHERE o.id IS NULL
        """;

        entities = session.createNativeQuery(sql, BookEntity.class)
                .setParameter(1, threshold)
                .getResultList();

        return entities;
    }
}
