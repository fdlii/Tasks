package com.yourcompany.mappers;

import java.util.List;

public interface IMapper<D, M, E> {
    M toModel(E entity);
    E toEntity(M model, boolean ignoreId);
    List<M> toModelsList(List<E> entities);
    D fromModelToDTO(M model);
    List<D> toDTOList(List<M> models);
}
