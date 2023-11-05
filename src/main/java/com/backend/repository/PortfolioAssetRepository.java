package com.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.backend.model.PortfolioAsset;

@RepositoryRestResource(exported = false)
public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {
    PortfolioAsset findByPortfolioAssetId(long PortfolioAssetId);
    List<PortfolioAsset> findAllByPortfolioId(long pid);
    PortfolioAsset findByPortfolioIdAndAssetTicker(long portfolioId, String assetTicker);
}
