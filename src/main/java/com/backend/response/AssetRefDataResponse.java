package com.backend.response;

import java.util.List;

import com.backend.model.AssetRefData;

import lombok.Data;

@Data
public class AssetRefDataResponse {
    List<AssetRefData> assetRefDataList;
}
