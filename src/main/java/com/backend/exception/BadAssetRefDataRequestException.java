package com.backend.exception;

public class BadAssetRefDataRequestException extends RuntimeException {
    public BadAssetRefDataRequestException() {
        super("Bad Request.");
    }
    public BadAssetRefDataRequestException(String message) {
        super("Bad Request: " + message);
    }
}

