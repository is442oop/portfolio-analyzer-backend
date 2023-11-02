package com.backend.exception;

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
