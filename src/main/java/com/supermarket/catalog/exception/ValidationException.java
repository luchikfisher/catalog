package com.supermarket.catalog.exception;

public class ValidationException extends ApiException {

    public ValidationException(String message) {
        super(message);
    }
}