package com.backend.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Request is malformed.");
    }

    public BadRequestException(String message) {
        super("Request is malformed: " + message);
    }
}
