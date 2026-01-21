package com.task_13.DAOs;

import com.task_13.HibernateConnector;
import com.task_13.entities.BookEntity;
import com.task_13.entities.RequestEntity;
import com.task_3_4.Book;
import com.task_3_4.Request;
import com.task_8_2.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookDAO extends GenericDAO<Book, Long, BookEntity> {
    @Inject
    private RequestDAO requestDAO;

    public BookDAO() {
        super(BookEntity.class);
    }

    @Override
    protected Book mapFromEntityToModel(BookEntity entity) {
        return new Book(
                (int) entity.getId(),
                entity.getName(),
                entity.getAuthor(),
                entity.getDescription(),
                Date.from(entity.getPublished().atZone(ZoneId.systemDefault()).toInstant()),
                entity.getCountInStock(),
                entity.getPrice()
        );
    }

    @Override
    protected BookEntity mapFromModelToEntity(Book model, boolean ignoreId) {
        BookEntity entity;
        if (ignoreId) {
            entity = new BookEntity(
                    model.getName(),
                    model.getAuthor(),
                    model.getDescription(),
                    model.getPublished().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    model.isInStock(),
                    model.getCountInStock(),
                    model.getPrice()
            );
        }
        else {
            entity = new BookEntity(
                    model.getId(),
                    model.getName(),
                    model.getAuthor(),
                    model.getDescription(),
                    model.getPublished().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    model.isInStock(),
                    model.getCountInStock(),
                    model.getPrice()
            );
        }
        List<RequestEntity> requestEntities = new ArrayList<>();
        for (Request request : model.getRequests()) {
            requestEntities.add(requestDAO.mapFromModelToEntity(request, false));
        }
        entity.setRequests(requestEntities);
        return entity;
    }

    public Book findByName(String bookName) {
        try (Session session = HibernateConnector.getSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = """
                    SELECT b
                    FROM BookEntity b
                    WHERE b.name = :bookname
                    """;

            BookEntity bookEntity = session.createQuery(hql, BookEntity.class)
                    .setParameter("bookname", bookName)
                    .getSingleResult();

            transaction.commit();
            session.close();
            return mapFromEntityToModel(bookEntity);
        }
    }
}
