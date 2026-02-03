package com.yourcompany.mappers;

import java.util.List;

public interface IMapper<M, E> {
    M toModel(E entity);
    E toEntity(M model, boolean ignoreId);
    List<M> toModelsList(List<E> entities);
}
