package com.backend.response;

import lombok.Data;

@Data
public class CreatePortfolioResponse {
    private long pid;
    private String userId;
    private String portfolioName;
    private String description;
}