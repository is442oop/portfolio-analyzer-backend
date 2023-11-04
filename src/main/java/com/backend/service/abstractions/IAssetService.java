package com.backend.service.abstractions;

import java.util.List;

import com.backend.model.Asset;

public interface IAssetService {
    List<Asset> findAll();
    Asset findByAssetId(long assetId);
}
