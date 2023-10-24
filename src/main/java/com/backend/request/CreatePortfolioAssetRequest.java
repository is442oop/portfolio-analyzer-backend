package com.backend.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreatePortfolioAssetRequest extends BaseRequest {
    private long portfolioId;
    private long assetId;
    private double averagePrice;
    private int quantity;
}
