package com.supermarket.catalog.exception;

public class EntityNotFoundException extends ApplicationException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}