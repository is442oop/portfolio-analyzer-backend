package com.backend.response;

import lombok.Data;

@Data
public class CreatePortfolioAssetResponse {
    private long portfolioId;
    private long assetId;
    private double averagePrice;
    private int quantity;
}