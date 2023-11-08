package com.backend.exception;

public class WatchlistNotFoundException extends RuntimeException {
    public WatchlistNotFoundException() {
        super("Watchlist not found");
    }

    public WatchlistNotFoundException(String UserId) {
        super("Could not find watchlist with the following ID: " + UserId);
    }

    // public WatchlistNotFoundException(long portfolioId, String assetTicker) {
    //     super("Could not find portfolio asset with the following portfolio ID " + portfolioId + " and ticker: "
    //             + assetTicker);
    // }
}
