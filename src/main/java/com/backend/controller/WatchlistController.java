package com.backend.controller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.backend.configuration.Constants;
import com.backend.configuration.Environment;
import com.backend.exception.BadRequestException;
import com.backend.model.Watchlist;
import com.backend.service.abstractions.IWatchlistService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.backend.request.CreateWatchlistRequest;
import com.backend.response.CreateWatchlistResponse;
import com.backend.response.GetWatchlistByIdResponse;
import com.backend.response.GetWatchlistPriceDataResponse;
import com.backend.request.UpdateWatchlistAssetsRequest;
import com.backend.response.UpdateWatchlistAssetsResponse;
import com.backend.request.RemoveWatchlistAssetRequest;
import com.backend.response.RemoveWatchlistAssetResponse;
import com.backend.exception.WatchlistNotFoundException;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class WatchlistController {
    Logger logger = LoggerFactory.getLogger(WatchlistController.class);
    private final IWatchlistService watchlistService;

    @Autowired
    public WatchlistController(IWatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

	public static Map<String, Object> getAssetDataOverview(String assetTicker, String apikey) throws Exception{
		Map<String, Object> output = new HashMap<>();
		// for overview -> MarketCapitalization and name
		assetTicker = assetTicker.replaceAll("\\s", "");
		String apiUrl = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + assetTicker
				+ "&apikey=" + apikey;
		HttpGet httpGet = new HttpGet(apiUrl);
		HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet);

        // Extract the JSON response as a String
        String jsonResponse = EntityUtils.toString(response.getEntity());

        Gson gson = new GsonBuilder().create();
        JsonObject jsonRes = gson.fromJson(jsonResponse, JsonObject.class);
        String assetName = jsonRes.getAsJsonPrimitive("Name").getAsString();
		long mktCap = jsonRes.getAsJsonPrimitive("MarketCapitalization").getAsLong();
		// put assetName and mktCap into a temp map to put into the output
		output.put("AssetOverview Name", assetName);
		output.put("AssetOverview MarketCapitalization", mktCap);

		return output;
	}

	public static Map<String, Map<String, Object>> getAssetDataIntraday(String assetTicker, String apikey) throws Exception{
		Map<String, Map<String, Object>> output = new HashMap<>();
		Map<String, Object> tempMap = new LinkedHashMap<>();

		// for hourly -> top 168 --> adjusted close
		assetTicker = assetTicker.replaceAll("\\s", "");
		String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="
				+ assetTicker + "&interval=60min&outputsize=full&apikey=" + apikey;
		HttpGet httpGet = new HttpGet(apiUrl);
		HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet);

        // Extract the JSON response as a String
        String jsonResponse = EntityUtils.toString(response.getEntity());

        Gson gson = new GsonBuilder().create();
        JsonObject jsonRes = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject assetHistPrices = jsonRes.getAsJsonObject("Time Series (60min)");

		int count = 0; 
		for (String dateStr : assetHistPrices.keySet()){
			if (count == 168){ // 168 hours in 7 days
				break;
			}
			JsonObject priceJson = assetHistPrices.getAsJsonObject(dateStr);
			double adjClosePrice = priceJson.getAsJsonPrimitive("4. close").getAsDouble();
			tempMap.put(dateStr, adjClosePrice);
			count += 1;
		}
		output.put("AssetIntraday", tempMap);
		
		return output;
	}

	public static Map<String, Object> getAssetDataGlobalQuote(String assetTicker, String apikey) throws Exception {
		Map<String, Object> output = new HashMap<>();
		// for global quote -> 5. and 6.
		assetTicker = assetTicker.replaceAll("\\s", "");
		String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + assetTicker
				+ "&apikey=" + apikey;
		HttpGet httpGet = new HttpGet(apiUrl);
		HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpGet);

        // Extract the JSON response as a String
        String jsonResponse = EntityUtils.toString(response.getEntity());

        Gson gson = new GsonBuilder().create();
        JsonObject jsonRes = gson.fromJson(jsonResponse, JsonObject.class);
		JsonObject assetGlobalQuote = jsonRes.getAsJsonObject("Global Quote");
        double price = assetGlobalQuote.getAsJsonPrimitive("05. price").getAsDouble();
        long volume = assetGlobalQuote.getAsJsonPrimitive("06. volume").getAsLong();
		output.put("Global Quote Price", price);
		output.put("Global Quote Volume", volume);

		return output;
	}

    @PostMapping(path = "/watchlist")
    public CreateWatchlistResponse createWatchlist(@RequestBody CreateWatchlistRequest request) {

		if (request.getWatchlistAssets() == null || request.getUserId()==null || request.getUserId().isEmpty()) {
			throw new BadRequestException(Constants.MESSAGE_MISSINGWATCHLISTDATA);
		}

		logger.info("Beginning creation of new watchlist with the following details");
		logger.info("Watchlist User ID: " + request.getUserId());
		logger.info("New Watchlist Assets: " + request.getWatchlistAssets());
		Watchlist watchlist = watchlistService.createWatchlist(
				new Watchlist(
						request.getUserId(),
						request.getWatchlistAssets()
                )
        );

		CreateWatchlistResponse response = new CreateWatchlistResponse();
		response.setWatchlistId(watchlist.getWid());
		response.setUserId(watchlist.getUid());
		response.setWatchlistAssets(watchlist.getAssets());

		return response;
    }

    // @GetMapping
    // public List<Watchlist> getAllWatchlists() {
    //     return watchlistService.getAllWatchlists();
    // }

    @GetMapping(path = "/watchlist/wid/{wid}")
    public GetWatchlistByIdResponse getWatchlistByWid(@PathVariable long wid) {
		Watchlist watchlist = watchlistService.findByWid(wid);

		// if (watchlist == null) {
		// 		throw new WatchlistNotFoundException(wid);
		// 	}
		GetWatchlistByIdResponse response = new GetWatchlistByIdResponse();
		response.setUserId(watchlist.getUid());
		response.setWatchlistId(watchlist.getWid());
		response.setWatchlist_assets(watchlist.getAssets());

		return response;
    }

	@GetMapping(path = "/watchlist/user/{uid}/prices")
    public GetWatchlistPriceDataResponse getWatchlistPricesByUid(@PathVariable String uid) throws Exception {
		Watchlist watchlist = watchlistService.findByUid(uid);

		// if (watchlist == null) {
		// 		throw new WatchlistNotFoundException(wid);
		// }

		List<String> assets = watchlist.getAssets();
		
		List<Map<String, Object>> output = new ArrayList<>();
	
		ExecutorService executor = Executors.newFixedThreadPool(30);
		List<Callable<Map<String, Object>>> overviewTasks = new ArrayList<>();

		for (String assetTicker : assets) {
			overviewTasks.add(() -> {
				Map<String, Object> assetNameData = getAssetDataOverview(assetTicker, Environment.getEnv("APIKEY"));
				Map<String, Map<String,Object>> intradayData = getAssetDataIntraday(assetTicker, Environment.getEnv("APIKEY"));
				Map<String, Object> globalQuoteData = getAssetDataGlobalQuote(assetTicker, Environment.getEnv("APIKEY"));
				Map<String, Object> priceData = new HashMap<>();
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.putAll(assetNameData);
				tempMap.putAll(intradayData);
				tempMap.putAll(globalQuoteData);
				priceData.put(assetTicker, tempMap);
				return priceData;
			});
		}
		List<Future<Map<String, Object>>> futures = executor.invokeAll(overviewTasks);
		for (Future<Map<String, Object>> future : futures) {
			Map<String, Object> priceData = future.get();
			output.add(priceData);
		}
		

		GetWatchlistPriceDataResponse response = new GetWatchlistPriceDataResponse();
		response.setWatchlistAssetPriceMap(output);

		return response;
    }


    @PutMapping(path = "/watchlist/wid/{wid}/add")
	public UpdateWatchlistAssetsResponse addAssetToWatchlist(@PathVariable long wid,
			@RequestBody UpdateWatchlistAssetsRequest body) {
		Watchlist watchlist = watchlistService.findByWid(wid);
		// if (watchlist == null) {
		// 	throw new PortfolioAssetNotFoundException(id);
		// }
		logger.info("Beginning update of watchlist with the following details");
		logger.info("Watchlist ID: " + wid);
		logger.info("New Asset Name: " + body.getWatchlist_asset());

		List<String> asset = body.getWatchlist_asset();
        List<String> assets = watchlist.getAssets();
		
		for (String a : asset) {
			assets.add(a);
		}

		if (asset != null && !asset.isEmpty()) {
			watchlist.setAssets(assets);
		}

		watchlistService.updateWatchlist(watchlist);

		logger.info("Watchlist updated successfully");

		UpdateWatchlistAssetsResponse response = new UpdateWatchlistAssetsResponse();
		response.setWatchlist_assets(watchlist.getAssets());

		return response;
	}

	@PutMapping(path = "/watchlist/user/{uid}/add")
	public UpdateWatchlistAssetsResponse addAssetToWatchlist(@PathVariable String uid,
			@RequestBody UpdateWatchlistAssetsRequest body) {
		Watchlist watchlist = watchlistService.findByUid(uid);
		// if (watchlist == null) {
		// 	throw new PortfolioAssetNotFoundException(id);
		// }
		logger.info("Beginning update of watchlist with the following details");
		logger.info("Watchlist ID: " + uid);
		logger.info("New Asset Name: " + body.getWatchlist_asset());

		List<String> asset = body.getWatchlist_asset();
        List<String> assets = watchlist.getAssets();
		
		for (String a : asset) {
			assets.add(a);
		}

		if (asset != null && !asset.isEmpty()) {
			watchlist.setAssets(assets);
		}

		watchlistService.updateWatchlist(watchlist);

		logger.info("Watchlist updated successfully");

		UpdateWatchlistAssetsResponse response = new UpdateWatchlistAssetsResponse();
		response.setWatchlist_assets(watchlist.getAssets());

		return response;
	}

    @PutMapping(path = "/watchlist/{wid}/remove")
    public RemoveWatchlistAssetResponse removeAssetFromWatchlist(@PathVariable long wid,
			@RequestBody RemoveWatchlistAssetRequest body) {
                Watchlist watchlist = watchlistService.findByWid(wid);
        //         if (watchlist == null) {
		// 	throw new PortfolioAssetNotFoundException(id);
		// }

        logger.info("Beginning update of watchlist with the following details");
		logger.info("Watchlist ID: " + wid);
		logger.info("New Asset Name: " + body.getWatchlist_asset());

        String asset = body.getWatchlist_asset();
        List<String> assets = watchlist.getAssets();
        assets.remove(asset);

        if (asset != null && !asset.isEmpty()) {
			watchlist.setAssets(assets);
		}

        watchlistService.updateWatchlist(watchlist);

        logger.info("Watchlist updated successfully");

        RemoveWatchlistAssetResponse response = new RemoveWatchlistAssetResponse();
		response.setWatchlist_assets(watchlist.getAssets());

                return response;
            }

	@PutMapping(path = "/watchlist/user/{uid}/remove")
    public RemoveWatchlistAssetResponse removeAssetFromWatchlist(@PathVariable String uid,
			@RequestBody RemoveWatchlistAssetRequest body) {
                Watchlist watchlist = watchlistService.findByUid(uid);
        //         if (watchlist == null) {
		// 	throw new PortfolioAssetNotFoundException(id);
		// }

        logger.info("Beginning update of watchlist with the following details");
		logger.info("Watchlist ID: " + uid);
		logger.info("New Asset Name: " + body.getWatchlist_asset());

        String asset = body.getWatchlist_asset();
        List<String> assets = watchlist.getAssets();
        assets.remove(asset);

        if (asset != null && !asset.isEmpty()) {
			watchlist.setAssets(assets);
		}

        watchlistService.updateWatchlist(watchlist);

        logger.info("Watchlist updated successfully");

        RemoveWatchlistAssetResponse response = new RemoveWatchlistAssetResponse();
		response.setWatchlist_assets(watchlist.getAssets());

                return response;
            }

	@GetMapping(path = "/watchlist/user/{uid}")
	public GetWatchlistByIdResponse getWatchlistByUid(@PathVariable String uid) {
		Watchlist watchlist = watchlistService.findByUid(uid);
		GetWatchlistByIdResponse response = new GetWatchlistByIdResponse();
		response.setUserId(watchlist.getUid());
		response.setWatchlistId(watchlist.getWid());
		response.setWatchlist_assets(watchlist.getAssets());

		return response;
	}
}