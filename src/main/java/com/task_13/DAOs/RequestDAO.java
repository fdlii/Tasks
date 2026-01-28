package com.task_13.DAOs;

import com.task_13.entities.RequestEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequestDAO extends GenericDAO<RequestEntity, Long> {

    public RequestDAO() {
        super(RequestEntity.class);
    }

    public List<RequestEntity> findRequestsByBookId(Long id) {
        String sql = """
                SELECT * FROM requests r
                WHERE r.bookid = ?
                """;

        Session session = hibernateConnector.getCurrentSession();
        List<RequestEntity> entities = session.createNativeQuery(sql, RequestEntity.class)
                .setParameter(1, id)
                .getResultList();

        return entities;
    }
}
