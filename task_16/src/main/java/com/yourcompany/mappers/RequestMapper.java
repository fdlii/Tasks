package com.yourcompany.mappers;

import com.yourcompany.models.Request;
import com.yourcompany.task_13.entities.RequestEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper implements IMapper<Request, RequestEntity> {
    @Autowired
    BookMapper bookMapper;

    @Override
    public Request toModel(RequestEntity entity) {
        return new Request(
                entity.getId(),
                bookMapper.toModel(entity.getBook()),
                entity.getCount(),
                entity.isOpen()
        );
    }

    @Override
    public RequestEntity toEntity(Request model, boolean ignoreId) {
        if (ignoreId) {
            return new RequestEntity(
                    bookMapper.toEntity(model.getBook(), false),
                    model.getCount(),
                    model.isOpen()
            );
        }
        return new RequestEntity(
                model.getId(),
                bookMapper.toEntity(model.getBook(), false),
                model.getCount(),
                model.isOpen()
        );
    }

    @Override
    public List<Request> toModelsList(List<RequestEntity> entities) {
        List<Request> requests = new ArrayList<>();
        for (RequestEntity requestEntity : entities) {
            requests.add(toModel(requestEntity));
        }
        return requests;
    }
}
