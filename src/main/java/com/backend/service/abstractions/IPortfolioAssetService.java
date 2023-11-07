package com.backend.service.abstractions;

import com.backend.model.PortfolioAsset;

import java.util.List;

public interface IPortfolioAssetService {
    List<PortfolioAsset> findAll();

    PortfolioAsset createNewPortfolioAsset(PortfolioAsset portfolioAsset);

    PortfolioAsset findByPortfolioAssetId(long PortfolioAssetId);

    List<PortfolioAsset> findAllByPortfolioId(long pid);

    void deletePortfolioAsset(long pid, String ticker);

    List<PortfolioAsset> findByPortfolioIdAndAssetTicker(long portfolioId, String assetTicker);

    List<PortfolioAsset> aggregatePortfolioAssets(List<PortfolioAsset> portfolioAssetList);
}