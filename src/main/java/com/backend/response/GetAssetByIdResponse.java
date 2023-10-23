package com.backend.response;

import com.backend.model.AssetRefData;
import com.backend.model.AssetType;
import java.util.List;

import lombok.Data;

@Data
public class GetAssetByIdResponse {
    private long assetId;
    private String assetTicker;
    private String assetName;
    private String assetDescription;
    private String assetIndustry;
    private AssetType assetType;
    private List<AssetRefData> assetRefDataList;
}
