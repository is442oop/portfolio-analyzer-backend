package com.backend.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreatePortfolioAssetRequest extends BaseRequest {
    private Long portfolioId;
    private String assetTicker;
    private double averagePrice;
    private int quantity;
    private long dateCreated;
    private long dateModified;
}
