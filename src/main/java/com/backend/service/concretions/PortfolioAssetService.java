package com.backend.service.concretions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.backend.model.Portfolio;
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
    public List<PortfolioAsset> findByPortfolioIdAndAssetTicker(long pid, String ticker) {
        logger.info("Searching for portfolio assets in portfolioID: {} and ticker: {}", pid, ticker);
        List<PortfolioAsset> portfolioAssets = repository.findByPortfolioIdAndAssetTicker(pid, ticker);

        if (portfolioAssets == null || portfolioAssets.isEmpty()) {
            throw new PortfolioAssetNotFoundException(pid, ticker);
        } else
            return portfolioAssets;
    }

    @Override
    public void deletePortfolioAsset(long pid, String ticker) {
        List<PortfolioAsset> portfolioAssets = repository.findByPortfolioIdAndAssetTicker(pid, ticker);
        for (PortfolioAsset portfolioAsset : portfolioAssets) {
            if (portfolioAsset == null) {
                throw new PortfolioAssetNotFoundException(pid, ticker);
            } else {
                repository.delete(portfolioAsset);
            }
        }
    }

    @Override
    public List<PortfolioAsset> aggregatePortfolioAssets(List<PortfolioAsset> portfolioAssetList) {
        Map<String, PortfolioAsset> aggregatedPortfolioAssets = portfolioAssetList.stream()
                .collect(Collectors.groupingBy(e -> e.getAssetTicker(), Collectors.collectingAndThen(
                        Collectors.toList(),
                        l -> l.stream().reduce(PortfolioAsset::merge).get())));
        List<PortfolioAsset> aggregatedPortfolioAssetsList = aggregatedPortfolioAssets.values().stream()
                .collect(Collectors.toList());
        return aggregatedPortfolioAssetsList;

    }
}
