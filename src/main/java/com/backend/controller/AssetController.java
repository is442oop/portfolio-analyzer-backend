package com.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.backend.model.Asset;
import com.backend.response.GetAssetByTickerResponse;
import com.backend.response.AssetResponse;
import com.backend.service.abstractions.IAssetService;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class AssetController {
    Logger logger = LoggerFactory.getLogger(AssetController.class);
    private final IAssetService assetService;

    @Autowired
    public AssetController(IAssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping(path = "/assets/{assetTicker}")
    public GetAssetByTickerResponse findByAssetTicker(@PathVariable("assetTicker") String assetTicker) {
        Asset asset = assetService.findByAssetTicker(assetTicker);

        GetAssetByTickerResponse response = new GetAssetByTickerResponse();
        // response.setAssetId(asset.getAssetId());
        response.setAssetTicker(asset.getAssetTicker());
        response.setAssetName(asset.getAssetName());
        response.setAssetDescription(asset.getAssetDescription());
        response.setAssetIndustry(asset.getAssetIndustry());
        response.setAssetType(asset.getAssetType());
        // response.setAssetRefDataList(asset.getAssetRefDataList());

        return response;
    }

    @GetMapping(path = "/assets")
    public List<AssetResponse> getAssets() {
        List<Asset> assetList = assetService.findAll();
        List<AssetResponse> responseList = new ArrayList<>();

        for (Asset asset : assetList) {
            AssetResponse response = new AssetResponse();
            response.setAssetName(asset.getAssetName());
            response.setAssetTicker(asset.getAssetTicker().trim());
            responseList.add(response);
        }
        return responseList;

    }
}
