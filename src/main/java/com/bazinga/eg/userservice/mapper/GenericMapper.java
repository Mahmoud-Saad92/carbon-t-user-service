package com.bazinga.eg.userservice.mapper;

public interface GenericMapper<T, R> {

    T toEntity(R dto);

    R toDto(T entity);
}
