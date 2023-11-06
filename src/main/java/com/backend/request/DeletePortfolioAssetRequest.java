package com.backend.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

public class DeletePortfolioAssetRequest {
    private Long portfolioId;
    private String assetTicker;
}