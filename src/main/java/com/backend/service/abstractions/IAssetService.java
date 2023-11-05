package com.backend.service.abstractions;

import java.util.List;

import com.backend.model.Asset;

public interface IAssetService {
    Asset findByAssetTicker(String assetTicker);
    List<Asset> findAll();
    Asset findByAssetId(long assetId);
}
