package com.backend.exception;

import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PortfolioNotFoundException extends RuntimeException {
    public PortfolioNotFoundException() {
        super("Portfolio not found");
    }

    public PortfolioNotFoundException(long pid) {
        super("Could not find portfolio with the following PID: " + pid);
    }

    public PortfolioNotFoundException(int pid) {
        super("Could not find portfolio with the following PID: " + pid);
    }

}
