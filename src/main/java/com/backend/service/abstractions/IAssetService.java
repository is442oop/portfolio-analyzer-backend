package com.backend.service.abstractions;

import com.backend.model.Asset;

public interface IAssetService {
    Asset findByAssetId(long assetId);
}
