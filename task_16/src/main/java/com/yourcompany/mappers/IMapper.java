package com.yourcompany.mappers;

import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.exceptions.ClientNotFoundException;
import com.yourcompany.exceptions.OrderNotFoundException;
import com.yourcompany.exceptions.RequestNotFoundException;

import java.util.List;

public interface IMapper<D, M, E> {
    M toModel(E entity) throws BookNotFoundException, ClientNotFoundException, OrderNotFoundException, RequestNotFoundException;
    E toEntity(M model, boolean ignoreId);
    List<M> toModelsList(List<E> entities) throws BookNotFoundException, ClientNotFoundException, RequestNotFoundException, OrderNotFoundException;
    D fromModelToDTO(M model);
    List<D> toDTOList(List<M> models);
}
