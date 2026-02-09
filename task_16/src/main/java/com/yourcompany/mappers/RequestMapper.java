package com.yourcompany.mappers;

import com.yourcompany.DTO.RequestDTO;
import com.yourcompany.entities.RequestEntity;
import com.yourcompany.models.Request;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestMapper implements IMapper<RequestDTO, Request, RequestEntity> {
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

    @Override
    public RequestDTO fromModelToDTO(Request model) {
        return new RequestDTO(
                model.getId(),
                model.getBook().getName(),
                model.getCount(),
                model.isOpen()
        );
    }

    @Override
    public List<RequestDTO> toDTOList(List<Request> models) {
        List<RequestDTO> requestDTOList = new ArrayList<>();
        for (Request request : models) {
            requestDTOList.add(fromModelToDTO(request));
        }
        return requestDTOList;
    }
}
