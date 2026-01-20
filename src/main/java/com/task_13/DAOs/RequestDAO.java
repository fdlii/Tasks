package com.task_13.DAOs;

import com.task_13.entities.RequestEntity;
import com.task_3_4.Request;

public class RequestDAO extends GenericDAO<Request, Long, RequestEntity> {
    private BookDAO bookDAO = new BookDAO();

    public RequestDAO() {
        super(RequestEntity.class);
    }

    @Override
    protected Request mapFromEntityToModel(RequestEntity entity) {
        Request model = new Request(
                entity.getId(),
                bookDAO.mapFromEntityToModel(entity.getBook()),
                entity.getCount(),
                entity.isOpen()
        );
        return model;
    }

    @Override
    protected RequestEntity mapFromModelToEntity(Request model) {
        RequestEntity entity = new RequestEntity(
                model.getId(),
                bookDAO.mapFromModelToEntity(model.getBook()),
                model.getCount(),
                model.isOpen()
        );
        return entity;
    }
}
