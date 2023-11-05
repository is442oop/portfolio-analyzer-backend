package com.backend.response;

import lombok.Data;

@Data
public class CreatePortfolioAssetResponse {
    private long portfolioId;
    private String assetTicker;
    private double averagePrice;
    private int quantity;
    private String dateCreated;
    private String dateModified;
}