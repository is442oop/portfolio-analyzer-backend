package com.backend.configuration;

public final class Constants {

    private Constants() {
    }

    public static final String MAIN_CURRENCY = "SGD";

    public static final String MESSAGE_MALFORMEDREQUEST = "Invalid request body or parameters.";
    public static final String MESSAGE_INVALIDUSERNAME = "Invalid username.";
    public static final String MESSAGE_INVALIDEMAIL = "Invalid email.";
    public static final String MESSAGE_SAMEUSEREXIST = "User already exists";
    public static final String MESSAGE_SAMEUSERNAMEEXIST = "User with the same name exists.";
    public static final String MESSAGE_SAMEEMAILEXIST = "User with the same email exists.";
    public static final String MESSAGE_MISSINGPORTFOLIONAME = "Portfolio name is missing.";
    public static final String MESSAGE_MISSINGPORTFOLIODESC = "Portfolio description is missing.";
    public static final String MESSAGE_INVALIDHISTORICALCALL = "Incorrect request. Appropriate query parameters are 1week, 1month, 6month, 1year, 5year and all";
    public static final String MESSAGE_INVALIDPORTFOLIOID = "Portfolio id is missing";
    public static final String MESSAGE_INVALIDASSETID = "Asset id is missing";
    public static final String MESSAGE_INVALIDASSETTICKER = "Asset ticker is missing";
    public static final String MESSAGE_MISSINGPORTFOLIOMETADATA = "Either portfolio name or description is required to update portfolio metadata";
    public static final String MESSAGE_MISSINGWATCHLISTDATA = "Either watchlist user id or watchlist assets is required to create watchlist";
}
