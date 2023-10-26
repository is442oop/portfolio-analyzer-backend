package com.backend.exception;

public class PortfolioAssetNotFoundException extends RuntimeException {
    public PortfolioAssetNotFoundException() {
        super("Portfolio asset not found");
    }
    public PortfolioAssetNotFoundException(long portfolioAssetId) {
        super("Could not find portfolio asset with the following ID: " + portfolioAssetId);
    }
}
