package com.backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
    public UserNotFoundException(String email) {
        super("Could not find user " + email);
    }

    public UserNotFoundException(long id) {
        super("User not found");
    }
}
