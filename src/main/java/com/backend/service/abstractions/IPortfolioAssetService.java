package com.backend.service.abstractions;

import java.util.List;

import com.backend.model.PortfolioAsset;

public interface IPortfolioAssetService {
    List<PortfolioAsset> findAll();
    PortfolioAsset createNewPortfolioAsset(PortfolioAsset portfolioAsset);
    PortfolioAsset findByPortfolioAssetId(long PortfolioAssetId);
    List<PortfolioAsset> findAllByPortfolioId(long pid);
    public PortfolioAsset deletePortfolioAsset(long pid, String ticker);
    public PortfolioAsset findByPortfolioIdAndAssetTicker(long portfolioId, String assetTicker);
}