package com.backend.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreatePortfolioRequest extends BaseRequest {
    private long userId;
    private String portfolioName;
    private String description;
}
