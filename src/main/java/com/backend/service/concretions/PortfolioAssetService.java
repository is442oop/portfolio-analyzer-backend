package com.backend.service.concretions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.model.PortfolioAsset;
import com.backend.repository.PortfolioAssetRepository;
import com.backend.exception.PortfolioNotFoundException;
import com.backend.exception.PortfolioAssetNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PortfolioAssetService implements com.backend.service.abstractions.IPortfolioAssetService {
    private final PortfolioAssetRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(PortfolioAssetService.class);

    @Autowired
    public PortfolioAssetService(PortfolioAssetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PortfolioAsset> findAll() {
        return repository.findAll();
    }

    @Override
    public PortfolioAsset createNewPortfolioAsset(PortfolioAsset portfolioAsset) {
        return repository.save(portfolioAsset);
    }

    @Override
    public List<PortfolioAsset> findAllByPortfolioId(long pid) {
        List<PortfolioAsset> portfolioAsset = repository.findAllByPortfolioId(pid);

        if (portfolioAsset == null) {
            throw new PortfolioNotFoundException(pid);
        } else
            return portfolioAsset;
    }

    @Override
    public PortfolioAsset findByPortfolioAssetId(long portfolioAssetId) {
        PortfolioAsset portfolioAsset = repository.findByPortfolioAssetId(portfolioAssetId);

        if (portfolioAsset == null) {
            throw new PortfolioAssetNotFoundException(portfolioAssetId);
        } else
            return portfolioAsset;
    }

    @Override
    public PortfolioAsset findByPortfolioIdAndAssetTicker(long pid, String ticker) {
        logger.info("Searching for portfolio asset with ID: {} and ticker: {}", pid, ticker);
        PortfolioAsset portfolioAsset = repository.findByPortfolioIdAndAssetTicker(pid, ticker);

        if (portfolioAsset == null) {
            logger.warn("PortfolioAssetNotFoundException thrown for ID: {} and ticker: {}", pid, ticker);
            throw new PortfolioAssetNotFoundException(pid, ticker);
        } else
            return portfolioAsset;
    }

    @Override
    public PortfolioAsset deletePortfolioAsset(long pid, String ticker) {
        PortfolioAsset portfolioAsset = repository.findByPortfolioIdAndAssetTicker(pid, ticker);

        if (portfolioAsset == null) {
            throw new PortfolioAssetNotFoundException(pid, ticker);
        } else {
            repository.delete(portfolioAsset);
            return portfolioAsset;
        }
    }

}
