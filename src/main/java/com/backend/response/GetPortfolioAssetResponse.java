package com.backend.response;

import com.backend.model.PortfolioAsset;

import lombok.Data;

@Data
public class GetPortfolioAssetResponse {
    private PortfolioAsset portfolioAsset;
}

