package com.yourcompany.mappers;

import java.util.List;

public interface IMapper<D, M, E> {
    M toModel(E entity);
    E toEntity(M model, boolean ignoreId);
    List<M> toModelsList(List<E> entities);
    M fromDTOtoModel(D DTO);
    D fromModelToDTO(M model);
}
