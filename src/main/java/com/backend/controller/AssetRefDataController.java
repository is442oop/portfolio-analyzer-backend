package com.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.backend.model.AssetRefData;
import com.backend.configuration.Constants;
import com.backend.response.AssetRefDataResponse;
import com.backend.service.abstractions.IAssetRefDataService;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class AssetRefDataController {
	Logger logger = LoggerFactory.getLogger(PortfolioController.class);
    private final IAssetRefDataService assetRefDataService;

    @Autowired
    public AssetRefDataController(IAssetRefDataService assetRefDataService) {
        this.assetRefDataService = assetRefDataService;
    }

	@GetMapping(path = "/asset/{assetId}")
    public AssetRefDataResponse getAssetRefData(@PathVariable long assetId){
        List<AssetRefData> assetList = assetRefDataService.findAllByAssetId(assetId);
        AssetRefDataResponse response = new AssetRefDataResponse();
        response.setAssetRefDataList(assetList);
        return response;
    }

    @GetMapping(path = "/asset/{assetId}/week")
    public AssetRefDataResponse getAssetRefDataWeek(@PathVariable long assetId){
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusWeeks(1).plusDays(1);
        System.out.println("Collecting data from " + startDate + " to " + endDate);
        List<AssetRefData> assetList = assetRefDataService.findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(assetId, startDate, endDate);
        System.out.println(assetList);
        AssetRefDataResponse response = new AssetRefDataResponse();
        response.setAssetRefDataList(assetList);
        return response;
    }

    @GetMapping(path = "/asset/{assetId}/month")
    public AssetRefDataResponse getAssetRefDataMonth(@PathVariable long assetId){
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusMonths(1).plusDays(1);
        System.out.println("Collecting data from " + startDate + " to " + endDate);
        List<AssetRefData> assetList = assetRefDataService.findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(assetId, startDate, endDate);
        System.out.println(assetList);
        AssetRefDataResponse response = new AssetRefDataResponse();
        response.setAssetRefDataList(assetList);
        return response;
    }

    @GetMapping(path = "/asset/{assetId}/6month")
    public AssetRefDataResponse getAssetRefDataSixMonth(@PathVariable long assetId){
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusMonths(6).plusDays(1);
        System.out.println("Collecting data from " + startDate + " to " + endDate);
        List<AssetRefData> assetList = assetRefDataService.findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(assetId, startDate, endDate);
        System.out.println(assetList);
        AssetRefDataResponse response = new AssetRefDataResponse();
        response.setAssetRefDataList(assetList);
        return response;
    }

    @GetMapping(path = "/asset/{assetId}/year")
    public AssetRefDataResponse getAssetRefDataYear(@PathVariable long assetId){
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusYears(1).plusDays(1);
        System.out.println("Collecting data from " + startDate + " to " + endDate);
        List<AssetRefData> assetList = assetRefDataService.findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(assetId, startDate, endDate);
        System.out.println(assetList);
        AssetRefDataResponse response = new AssetRefDataResponse();
        response.setAssetRefDataList(assetList);
        return response;
    }

    @GetMapping(path = "/asset/{assetId}/5year")
    public AssetRefDataResponse getAssetRefDataFiveYears(@PathVariable long assetId){
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.minusYears(5).plusDays(1);
        System.out.println("Collecting data from " + startDate + " to " + endDate);
        List<AssetRefData> assetList = assetRefDataService.findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(assetId, startDate, endDate);
        System.out.println(assetList);
        AssetRefDataResponse response = new AssetRefDataResponse();
        response.setAssetRefDataList(assetList);
        return response;
    }

}
