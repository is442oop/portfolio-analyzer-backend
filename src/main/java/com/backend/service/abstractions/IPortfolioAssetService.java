package com.backend.service.abstractions;

import java.util.List;

import com.backend.model.PortfolioAsset;

public interface IPortfolioAssetService {
    List<PortfolioAsset> findAll();
    PortfolioAsset createNewPortfolioAsset(PortfolioAsset portfolioAsset);
    PortfolioAsset findByPortfolioAssetId(long PortfolioAssetId);
    List<PortfolioAsset> findAllByPortfolioId(long pid);
    public void deletePortfolioAsset(long pid, String ticker);
    public List<PortfolioAsset> findByPortfolioIdAndAssetTicker(long portfolioId, String assetTicker);
    List<PortfolioAsset> aggregatePortfolioAssets(List<PortfolioAsset> portfolioAssetList);
}