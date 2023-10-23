package com.backend.response;

import com.backend.model.AssetType;

import lombok.Data;

@Data
public class GetAssetByIdResponse {
    private long assetId;
    private String assetTicker;
    private String assetName;
    private String assetDescription;
    private String assetIndustry;
    private AssetType assetType;

}
