package org.example.currencyexchangeproject.Exceptions;

public class NotFoundDataException extends RuntimeException {
    public NotFoundDataException(String message) {
        super(message);
    }
}
