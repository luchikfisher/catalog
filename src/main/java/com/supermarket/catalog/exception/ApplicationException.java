package com.supermarket.catalog.exception;

public abstract class ApplicationException extends Exception {

    protected ApplicationException(String message) {
        super(message);
    }
}