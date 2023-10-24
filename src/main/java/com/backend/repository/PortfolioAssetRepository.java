package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.backend.model.PortfolioAsset;

@RepositoryRestResource(exported = false)
public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {
    PortfolioAsset findByPortfolioAssetId(long PortfolioAssetId);
    <List>PortfolioAsset findAllByPortfolioId(long portfolioId);
}
