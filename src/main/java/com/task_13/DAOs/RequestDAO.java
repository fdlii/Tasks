package com.task_13.DAOs;

import com.task_13.entities.RequestEntity;
import com.task_3_4.Request;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RequestDAO extends GenericDAO<Request, Long, RequestEntity> {
    private final BookDAO bookDAO;

    public RequestDAO(BookDAO bookDAO) {
        super(RequestEntity.class);
        this.bookDAO = bookDAO;
    }

    @Override
    protected Request mapFromEntityToModel(RequestEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Request(
                entity.getId(),
                bookDAO.mapFromEntityToModel(entity.getBook()),
                entity.getCount(),
                entity.isOpen()
        );
    }

    @Override
    protected RequestEntity mapFromModelToEntity(Request model, boolean ignoreId) {
        RequestEntity entity;
        if (ignoreId) {
            entity = new RequestEntity(
                    bookDAO.mapFromModelToEntity(model.getBook(), false),
                    model.getCount(),
                    model.isOpen()
            );
        }
        else {
            entity = new RequestEntity(
                    model.getId(),
                    bookDAO.mapFromModelToEntity(model.getBook(), false),
                    model.getCount(),
                    model.isOpen()
            );
        }
        return entity;
    }

    public List<Request> findRequestsByBookId(Long id) {
        String sql = """
                SELECT * FROM requests r
                WHERE r.bookid = ?
                """;

        Transaction tx = null;
        List<Request> requests;
        try (Session session = hibernateConnector.getSession()) {
            tx = session.beginTransaction();
            List<RequestEntity> requestEntities = session.createNativeQuery(sql, RequestEntity.class)
                    .setParameter(1, id)
                    .getResultList();
            tx.commit();
            requests = new ArrayList<>();
            for (RequestEntity requestEntity : requestEntities) {
                requests.add(mapFromEntityToModel(requestEntity));
            }
        }
        catch (HibernateException ex) {
            tx.rollback();
            throw new HibernateException(ex);
        }
        return requests;
    }
}
