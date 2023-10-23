package com.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.backend.model.Asset;
// import com.backend.configuration.Constants;
import com.backend.response.GetAssetByIdResponse;
import com.backend.service.abstractions.IAssetService;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class AssetController {
    Logger logger = LoggerFactory.getLogger(AssetController.class);
    private final IAssetService assetServce;

    @Autowired
    public AssetController (IAssetService assetService) {
        this.assetServce = assetService;
    }

    @GetMapping(path = "/asset/{assetId}")
    public GetAssetByIdResponse getAssetById(@PathVariable long assetId) {
        Asset asset = assetServce.findByAssetId(assetId);

        GetAssetByIdResponse response = new GetAssetByIdResponse();
        response.setAssetId(asset.getAssetId());
        response.setAssetTicker(asset.getAssetTicker());
        response.setAssetName(asset.getAssetName());
        response.setAssetDescription(asset.getAssetDescription());
        response.setAssetIndustry(asset.getAssetIndustry());
        response.setAssetType(asset.getAssetType());

        return response;
    }
}
