package com.backend.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdatePortfolioMetaDataRquest extends BaseRequest {
    String portfolioName;
    String description;
}
