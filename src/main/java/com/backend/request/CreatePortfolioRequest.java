package com.backend.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreatePortfolioRequest extends BaseRequest {
    private long id;
    private String portfolioName;
    private String description;
    private LocalDate date;
}
