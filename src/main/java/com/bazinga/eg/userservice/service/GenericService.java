package com.bazinga.eg.userservice.service;

import com.bazinga.eg.userservice.exception.model.EntityMappingException;
import com.bazinga.eg.userservice.mapper.GenericMapper;

public abstract class GenericService<T, R> {

    private final GenericMapper<T, R> mapper;

    protected GenericService(GenericMapper<T, R> mapper) {
        this.mapper = mapper;
    }

    public T mapToEntity(R dto) {
        T entity;

        try {
            entity = mapper.toEntity(dto);
        } catch (Exception e) {
            throw new EntityMappingException("wrong mapping occurs", e);
        }

        return entity;
    }

    public R mapToDto(T contact) {
        R dto;

        try {
            dto = mapper.toDto(contact);
        } catch (Exception e) {
            throw new EntityMappingException("wrong mapping occurs", e);
        }

        return dto;
    }
}
