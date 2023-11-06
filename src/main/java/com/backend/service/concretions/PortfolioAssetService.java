package com.backend.service.concretions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.model.Portfolio;
import com.backend.model.PortfolioAsset;
import com.backend.repository.PortfolioAssetRepository;
import com.backend.exception.PortfolioNotFoundException;
import com.backend.exception.PortfolioAssetNotFoundException;

@Service
public class PortfolioAssetService implements com.backend.service.abstractions.IPortfolioAssetService {
    private final PortfolioAssetRepository repository;

    @Autowired
    public PortfolioAssetService(PortfolioAssetRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PortfolioAsset> findAll() {
        return repository.findAll();
    }

    @Override
    public PortfolioAsset createNewPortfolioAsset(PortfolioAsset portfolioAsset){
        return repository.save(portfolioAsset);
    }

    @Override
    public List<PortfolioAsset> findAllByPortfolioId(long pid){
        List<PortfolioAsset> portfolioAsset = repository.findAllByPortfolioId(pid);

        if (portfolioAsset == null){
            throw new PortfolioNotFoundException(pid);
        }
        else
            return portfolioAsset;
    }

    @Override
    public PortfolioAsset findByPortfolioAssetId(long portfolioAssetId){
        PortfolioAsset portfolioAsset = repository.findByPortfolioAssetId(portfolioAssetId);

        if (portfolioAsset == null){
            throw new PortfolioAssetNotFoundException(portfolioAssetId);
        }
        else
            return portfolioAsset;
    }

    @Override 
    public List<PortfolioAsset> aggregatePortfolioAssets(List<PortfolioAsset> portfolioAssetList) {
        Map<String, PortfolioAsset> aggregatedPortfolioAssets = portfolioAssetList.stream()
                        .collect(Collectors.groupingBy(e -> e.getAssetTicker(), Collectors.collectingAndThen(
                                Collectors.toList(),
                                l -> l.stream().reduce(PortfolioAsset::merge).get())));
        List<PortfolioAsset> aggregatedPortfolioAssetsList = aggregatedPortfolioAssets.values().stream().collect(Collectors.toList());
        return aggregatedPortfolioAssetsList;

    }
}
