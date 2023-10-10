package com.backend.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreatePortfolioResponse {
    private long pid;
    private long userId;
    private String portfolioName;
    private String description;
    private LocalDate creationDate;
}