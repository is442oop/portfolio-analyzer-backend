package com.backend.response;

import lombok.Data;

@Data
public class GetPortfolioByIdResponse {
    private long pid;
    private String userId;
    private String portfolioName;
    private String description;
}
