package com.romankryvolapov.resolve.resolve.models.exceptions;

public class NotFoundServiceException extends AppException {
    public NotFoundServiceException(String entity, Long id) {
        super(entity + " with id=" + id + " not found");
    }
}