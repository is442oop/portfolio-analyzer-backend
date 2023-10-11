package com.backend.response;

import java.time.LocalDate;
import lombok.Data;

@Data
public class GetPortfolioByIdResponse {
    private long pid;
    private long userId;
    private String portfolioName;
    private String description;
    private LocalDate creationDate;
}
