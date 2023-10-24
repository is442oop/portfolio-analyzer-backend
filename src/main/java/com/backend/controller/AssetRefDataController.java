package com.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.backend.model.AssetRefData;
import com.backend.configuration.Constants;
import com.backend.exception.BadAssetRefDataRequestException;
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

	// @GetMapping(path = "/asset/info/{assetId}")
    // public AssetRefDataResponse getAssetRefData(@PathVariable long assetId){
    //     List<AssetRefData> assetList = assetRefDataService.findAllByAssetId(assetId);
    //     AssetRefDataResponse response = new AssetRefDataResponse();
    //     response.setAssetRefDataList(assetList);
    //     return response;
    // }

    @GetMapping(path = "/asset/ref/{assetId}")
    public AssetRefDataResponse getHistoricalAssetRefData(@PathVariable("assetId") long assetId, @RequestParam("duration") String duration){
        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate;
        System.out.println(duration);
        switch (duration){
            case "1week":
                startDate = endDate.minusWeeks(1).plusDays(1);
                break;
            case "1month":
                startDate = endDate.minusMonths(1).plusDays(1);
                break;
            case "6month":
                startDate = endDate.minusMonths(6).plusDays(1);
                break;
            case "1year":
                startDate = endDate.minusYears(1).plusDays(1);
                break;
            case "5year":
                startDate = endDate.minusYears(5).plusDays(1);
                break;
            case "all":
                System.out.println("Collecting ALL asset reference data...");
                List<AssetRefData> assetList = assetRefDataService.findAllByAssetId(assetId);
                AssetRefDataResponse response = new AssetRefDataResponse();
                response.setAssetRefDataList(assetList);
                System.out.println("ALL asset reference data collected.");
                return response;
            default:
                startDate = null;
        }

        if (startDate==null){
            throw new BadAssetRefDataRequestException(Constants.MESSAGE_INVALIDHISTORICALCALL);
        }

        System.out.println("Collecting data from " + startDate + " to " + endDate + "...");

        List<AssetRefData> assetList = assetRefDataService.findByAssetIdAndDayRecordBetweenOrderByDayRecordDesc(assetId, startDate, endDate);
        AssetRefDataResponse response = new AssetRefDataResponse();
        response.setAssetRefDataList(assetList);
        System.out.println("Data from " + startDate + " to " + endDate + " successfully collected.");
        return response;
    }
}
