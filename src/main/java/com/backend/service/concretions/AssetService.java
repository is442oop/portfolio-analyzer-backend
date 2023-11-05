package com.backend.service.concretions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.model.Asset;
import com.backend.repository.AssetRepository;

@Service
public class AssetService implements com.backend.service.abstractions.IAssetService {
    private final AssetRepository repository;

    @Autowired
    public AssetService(AssetRepository repository) {
        this.repository = repository;
    }

    @Override
    public Asset findByAssetTicker(String assetTicker){
        Asset asset = repository.findByAssetTicker(assetTicker);
        return asset;
    }

    @Override
    public List<Asset> findAll() {
        return repository.findAll();
    }
    
    @Override
    public Asset findByAssetId(long assetId) {
        Asset asset = repository.findByAssetId(assetId);
        if (asset == null) {
            throw new AssetNotFoundException(assetId);
        } else {
            return asset;
        }
    }

}
