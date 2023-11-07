package com.backend.response;

import java.util.List;

import com.backend.model.PortfolioAsset;

import lombok.Data;

@Data
public class GetAllPortfolioAssetsByUserResponse {
    private List<PortfolioAsset> portfolioAssetList;
}