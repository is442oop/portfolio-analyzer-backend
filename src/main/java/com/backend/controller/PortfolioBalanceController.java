package com.backend.controller;

import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.antlr.v4.runtime.tree.Tree;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import io.github.cdimascio.dotenv.Dotenv;

import com.backend.model.PortfolioAsset;
import com.backend.service.abstractions.IPortfolioAssetService;
import com.backend.response.GetPortfolioBalanceResponse;
 
@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class PortfolioBalanceController {
    private final IPortfolioAssetService portfolioAssetService;

    @Autowired
	public PortfolioBalanceController(IPortfolioAssetService portfolioAssetService) {
		this.portfolioAssetService = portfolioAssetService;
	}

    public List<String> getAssetTickerList(int pid){
        List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
        List<String> output = new ArrayList<>();
        for (PortfolioAsset portfolioAsset : portfolioAssetList){
            String ticker = portfolioAsset.getAssetTicker();
            if (!output.contains(ticker)){
                output.add(ticker);
            }
        }
        return output;
    }

    public static long roundEpochToCurrentDay(long epochTime) {
        Date date = new Date(epochTime * 1000L);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date updatedDate = calendar.getTime();
        long updatedEpochTime = updatedDate.getTime() / 1000L;

        return updatedEpochTime;
    }

    public TreeMap<Long, Map<String, Integer>> getHistoricalQty(long pid){
        TreeMap<Long, Map<String, Integer>> output = new TreeMap<>();
        
        Map<String, Integer> assetTruth = new HashMap<>();
        Map<Long, PortfolioAsset> epochMap = new TreeMap<>();       // map to sort portfolioAsset by dateCreated

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long yesterdayEpochTime = roundEpochToCurrentDay(calendar.getTime().getTime() / 1000L);
        calendar.add(Calendar.DAY_OF_MONTH, -89);
        long sevenDaysAgoEpochTime = roundEpochToCurrentDay(calendar.getTime().getTime() / 1000L);

        List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
        for (PortfolioAsset portfolioAsset : portfolioAssetList){
            long dateAdded = portfolioAsset.getDateCreated();

            epochMap.put(roundEpochToCurrentDay(dateAdded), portfolioAsset);            
        }
        for (long epochDate : epochMap.keySet()){
            PortfolioAsset portfolioAsset = epochMap.get(epochDate);
            String ticker = portfolioAsset.getAssetTicker().replaceAll("\\s", "");
            int qty = portfolioAsset.getQuantity();
            if (!assetTruth.containsKey(ticker)){
                assetTruth.put(ticker, qty);
            } else {
                int newQty = assetTruth.get(ticker) + qty;
                assetTruth.put(ticker, newQty);
            }
            Map<String, Integer> assetTruthCopy = new HashMap<>(assetTruth);
            output.put(epochDate, assetTruthCopy);
        }
        if (!output.containsKey(sevenDaysAgoEpochTime)){
            sevenDaysAgoEpochTime = output.firstKey();
        }

        Map<String, Integer> temp = output.get(output.firstKey());
        for (long epochTime = sevenDaysAgoEpochTime; epochTime <= yesterdayEpochTime; epochTime += 86400){
            if (!output.containsKey(epochTime)){
                output.put(epochTime, temp);
            }
            else{
                temp = output.get(epochTime);
                output.put(epochTime, temp);
            }
        }
        return output;
    }

    public static LinkedHashMap<Long, Double> getHistoricalPrice(String ticker, String key, int days) throws Exception{
        HttpClient httpClient = HttpClients.createDefault();
        String tickerCleaned = ticker.replaceAll("\\s", "");
        String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&apikey=" + key + "&symbol=" + tickerCleaned;
        HttpGet httpGet = new HttpGet(apiUrl);
        // Execute the GET request and get the response
        HttpResponse response = httpClient.execute(httpGet);

        // Extract the JSON response as a String
        String jsonResponse = EntityUtils.toString(response.getEntity());

        Gson gson = new GsonBuilder().create();
        JsonObject jsonRes = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject histPrices = jsonRes.getAsJsonObject("Time Series (Daily)");

        Set<String> keys = histPrices.keySet();
        Iterator<String> dateIterator = keys.iterator();

        LinkedHashMap<Long, Double> output = new LinkedHashMap<>();

        int counter = 0;
        while (dateIterator.hasNext()) {
            counter += 1;
            if (counter>days){
                break;
            }
            String dateClose = dateIterator.next();
            JsonObject priceDate = histPrices.getAsJsonObject(dateClose);
            double priceString = priceDate.get("5. adjusted close").getAsDouble();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateClose);
            long epoch = date.getTime() / 1000;

            output.put(epoch, priceString);
        }
        return output;
    }


    @GetMapping(path = "/portfolio/balance")
    public GetPortfolioBalanceResponse getPortfolioBalance(@RequestParam("duration") String duration, @RequestParam("pid") int pid) throws Exception {
        Map<String, Map<Long, Double>> assetPriceMap = new LinkedHashMap<>(); // Store daily adjusted close price for each asset
        Map<Long, Double> output = new LinkedHashMap<>(); // Store qty of each asset for each day

        List<String> tickers = getAssetTickerList(pid);
        int durationInt = Integer.parseInt(duration);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Callable<Map<Long, Double>>> tasks = new ArrayList<>();

        Dotenv dotenv = Dotenv.configure().load();
        String apikey = dotenv.get("APIKEY");
        System.out.println("API: " + apikey);

        for (String ticker : tickers) {
            Callable<Map<Long, Double>> task = () -> {
                Map<Long, Double> priceData = getHistoricalPrice(ticker, apikey, durationInt);
                return priceData;
            };
            tasks.add(task);

        }
        int counter = 0;
        List<Future<Map<Long, Double>>> futures = executor.invokeAll(tasks);
        for (Future<Map<Long, Double>> future : futures) {
            Map<Long, Double> priceData = future.get();
            String ticker = tickers.get(counter).replaceAll("\\s", "");

            assetPriceMap.put(ticker, priceData);
            counter += 1;
        }
        executor.shutdown();

        TreeMap<Long, Map<String, Integer>> qtyMap = getHistoricalQty(pid);
        TreeMap<Long, Map<String, Integer>> sortedQtyMap = new TreeMap<>(Collections.reverseOrder());
        TreeMap<Long, Map<String, Integer>> finalQtyMap = new TreeMap<>();
        sortedQtyMap.putAll(qtyMap);

        int i = 0;
        for (long epoch : sortedQtyMap.keySet()){
            if (i==durationInt){
                break;
            }
            finalQtyMap.put(epoch, sortedQtyMap.get(epoch));
            i += 1;
        }

        double temp = 0;
        for (long dateEpoch : finalQtyMap.keySet()){
            Map<String, Integer> assetQty = finalQtyMap.get(dateEpoch);
            double dailyBalance = 0;
            for (String ticker : assetQty.keySet()){
                int qty = assetQty.get(ticker);
                if (assetPriceMap.get(ticker).containsKey(dateEpoch)){
                    double price = assetPriceMap.get(ticker).get(dateEpoch);
                    dailyBalance += qty * price;
                }
            }
            if (dailyBalance == 0){
                dailyBalance = temp;
            }
            else{
            temp = dailyBalance;
            }
            output.put(dateEpoch, dailyBalance);
        }


        List<HashMap<String, Object>> portfolioHistoryData = new ArrayList<>();
        for (long epoch : output.keySet()){
            HashMap<String, Object> tempMap = new HashMap<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            Date dateEpoch = new Date(epoch * 1000);
            String dateStr = sdf.format(dateEpoch);


            tempMap.put("date", dateStr);
            tempMap.put("balance", output.get(epoch));
            portfolioHistoryData.add(tempMap);
        }

        GetPortfolioBalanceResponse response = new GetPortfolioBalanceResponse();
        response.setPortfolioHistoryData(portfolioHistoryData);

        return response;
    }
}
