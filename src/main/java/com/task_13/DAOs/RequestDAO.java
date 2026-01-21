package com.task_13.DAOs;

import com.task_13.HibernateConnector;
import com.task_13.entities.RequestEntity;
import com.task_3_4.Request;
import com.task_8_2.annotations.Inject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class RequestDAO extends GenericDAO<Request, Long, RequestEntity> {
    @Inject
    private BookDAO bookDAO;

    public RequestDAO() {
        super(RequestEntity.class);
    }

    @Override
    protected Request mapFromEntityToModel(RequestEntity entity) {
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

        try (Session session = HibernateConnector.getSession()) {
            Transaction tx = session.beginTransaction();
            List<RequestEntity> requestEntities = session.createNativeQuery(sql, RequestEntity.class)
                    .setParameter(1, id)
                    .getResultList();
            tx.commit();
            List<Request> requests = new ArrayList<>();
            for (RequestEntity requestEntity : requestEntities) {
                requests.add(mapFromEntityToModel(requestEntity));
            }

            return requests;
        }
        catch (HibernateException ex) {
            throw new HibernateException(ex);
        }
    }
}
