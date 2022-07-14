package com.bazinga.eg.userservice.exception.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EntityPersistException extends RuntimeException {

    public EntityPersistException(String message) {
        super(message);
    }

    public EntityPersistException(String message, Throwable cause) {
        super(message, cause);
    }
}
