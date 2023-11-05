package com.backend.response;

import com.backend.model.AssetType;

import lombok.Data;

@Data
public class GetAssetByTickerResponse {
    private String assetTicker;
    private String assetName;
    private String assetDescription;
    private String assetIndustry;
    private AssetType assetType;
}
