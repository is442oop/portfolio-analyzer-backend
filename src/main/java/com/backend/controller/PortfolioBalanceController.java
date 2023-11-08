package com.backend.controller;

import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.backend.configuration.Environment;
import com.backend.exception.PortfolioAssetNotFoundException;
import com.backend.exception.PortfolioNotFoundException;
import com.backend.model.Portfolio;
import com.backend.model.PortfolioAsset;
import com.backend.service.abstractions.IPortfolioAssetService;
import com.backend.service.abstractions.IUserService;
import com.backend.response.GetPortfolioBalanceResponse;
import com.backend.response.GetUserOverallPortfolioBalanceResponse;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class PortfolioBalanceController {
    private final IPortfolioAssetService portfolioAssetService;
    private final IUserService userService;;

    @Autowired
    public PortfolioBalanceController(IPortfolioAssetService portfolioAssetService, IUserService userService) {
        this.portfolioAssetService = portfolioAssetService;
        this.userService = userService;
    }

    public List<String> getAssetTickerListByPid(long pid) {
        List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
        Set<String> tickers = new HashSet<String>();
        for (PortfolioAsset portfolioAsset : portfolioAssetList) {
            tickers.add(portfolioAsset.getAssetTicker());
        }
        return new ArrayList<String>(tickers);
    }

    public List<String> getAssetTickerListByUserId(String userId) {
        List<Portfolio> portfolios = userService.findUserPortfolios(userId);
        Set<String> tickers = new HashSet<String>();
        for (Portfolio portfolio : portfolios) {
            tickers.addAll(getAssetTickerListByPid(portfolio.getPid()));
        }
        return new ArrayList<String>(tickers);
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

    public TreeMap<Long, Map<String, Integer>> getHistoricalQty(long pid) {
        TreeMap<Long, Map<String, Integer>> output = new TreeMap<>();
        Map<String, Integer> assetTruth = new HashMap<>();
        Map<Long, PortfolioAsset> epochMap = new TreeMap<>(); // map to sort portfolioAsset by dateCreated

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long yesterdayEpochTime = roundEpochToCurrentDay(calendar.getTime().getTime() / 1000L);
        calendar.add(Calendar.DAY_OF_MONTH, -89);
        long sevenDaysAgoEpochTime = roundEpochToCurrentDay(calendar.getTime().getTime() / 1000L);

        List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
        if (portfolioAssetList.isEmpty()) {
            return output;
        }

        for (PortfolioAsset portfolioAsset : portfolioAssetList) {
            long dateAdded = portfolioAsset.getDateCreated();
            epochMap.put(roundEpochToCurrentDay(dateAdded), portfolioAsset);
        }
        for (long epochDate : epochMap.keySet()) {
            PortfolioAsset portfolioAsset = epochMap.get(epochDate);
            String ticker = portfolioAsset.getAssetTicker().replaceAll("\\s", "");
            int qty = portfolioAsset.getQuantity();
            if (!assetTruth.containsKey(ticker)) {
                assetTruth.put(ticker, qty);
            } else {
                int newQty = assetTruth.get(ticker) + qty;
                assetTruth.put(ticker, newQty);
            }
            Map<String, Integer> assetTruthCopy = new HashMap<>(assetTruth);
            output.put(epochDate, assetTruthCopy);
        }
        if (!output.containsKey(sevenDaysAgoEpochTime)) {
            sevenDaysAgoEpochTime = output.firstKey();
        }

        Map<String, Integer> temp = output.get(output.firstKey());
        for (long epochTime = sevenDaysAgoEpochTime; epochTime <= yesterdayEpochTime; epochTime += 86400) {
            if (!output.containsKey(epochTime)) {
                output.put(epochTime, temp);
            } else {
                temp = output.get(epochTime);
                output.put(epochTime, temp);
            }
        }
        return output;
    }

    public static Map<Long, Double> getHistoricalPrice(String ticker, String key, int days) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        String tickerCleaned = ticker.replaceAll("\\s", "");
        String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&apikey=" + key
                + "&symbol="
                + tickerCleaned;
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

        Map<Long, Double> output = new LinkedHashMap<>();
        int counter = 0;
        while (dateIterator.hasNext()) {
            counter += 1;
            if (counter > days) {
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

    public static Map<String, Map<Long, Double>> requestPriceTickers(List<String> tickers, int days)
            throws Exception {
        Map<String, Map<Long, Double>> assetPriceMap = new LinkedHashMap<>(); // Store daily adjusted close price for
                                                                              // each asset
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Callable<Map<Long, Double>>> tasks = new ArrayList<>();
        for (String ticker : tickers) {
            tasks.add(() -> {
                Map<Long, Double> priceData = getHistoricalPrice(ticker, Environment.getEnv("APIKEY"), days);
                return priceData;
            });
        }
        int tickerIndex = 0;
        List<Future<Map<Long, Double>>> futures = executor.invokeAll(tasks);
        for (Future<Map<Long, Double>> future : futures) {
            Map<Long, Double> priceData = future.get();
            String ticker = tickers.get(tickerIndex++).replaceAll("\\s", "");
            assetPriceMap.put(ticker, priceData);
        }
        executor.shutdown();
        return assetPriceMap;
    }

    public List<Map<String, Object>> getPortfolioHistoryData(long pid, int days,
            Map<String, Map<Long, Double>> assetPriceMap) throws Exception {
        List<Map<String, Object>> portfolioHistoryData = new ArrayList<Map<String, Object>>(); // Store qty of each
                                                                                               // asset for each day
        TreeMap<Long, Map<String, Integer>> qtyMap = getHistoricalQty(pid);
        double prevDayBalance = 0;
        Iterator<Map.Entry<Long, Map<String, Integer>>> qMapIt = qtyMap.descendingMap().entrySet().iterator();
        Date dateEpoch = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dateEpoch);
        c.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        for (int i = 0; i < days + 1; i++) {
            // to backfill dates without tickers
            if (!qMapIt.hasNext() && i + 2 < days) {
                c.setTime(dateEpoch);
                for (int j = 0; j < days - i; j++) {
                    c.add(Calendar.DATE, -1);
                    String date = sdf.format(c.getTime());
                    Map<String, Object> m = new HashMap<>();
                    m.put("date", date);
                    m.put("balance", 0.);
                    portfolioHistoryData.add(m);
                }
                break;
            }
            Map.Entry<Long, Map<String, Integer>> e = qMapIt.next();
            long epoch = e.getKey();
            Map<String, Integer> assetQty = e.getValue();
            double dailyBalance = 0;
            for (Map.Entry<String, Integer> assetQtyEntry : assetQty.entrySet()) {
                String ticker = assetQtyEntry.getKey().replaceAll("\\s", "");
                int qty = assetQtyEntry.getValue();
                if (assetPriceMap.get(ticker).containsKey(epoch)) {
                    double price = assetPriceMap.get(ticker).get(epoch);
                    dailyBalance += qty * price;
                }
            }
            if (dailyBalance == 0.) {
                if (prevDayBalance == 0.) {
                    continue;
                }
                dailyBalance = prevDayBalance;
            } else {
                prevDayBalance = dailyBalance;
            }
            dateEpoch = new Date(epoch * 1000L);
            String dateStr = sdf.format(dateEpoch);
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("date", dateStr);
            tempMap.put("balance", dailyBalance);
            portfolioHistoryData.add(tempMap);
        }
        Collections.reverse(portfolioHistoryData);
        return portfolioHistoryData;
    }

    @GetMapping(path = "/portfolios/{pid}/balance")
    public GetPortfolioBalanceResponse getPortfolioBalance(@PathVariable Long pid,
            @RequestParam("duration") Integer days) throws Exception {
        List<String> tickers = getAssetTickerListByPid(pid);
        if (tickers.isEmpty()) {
            throw new PortfolioAssetNotFoundException();
        }
        Map<String, Map<Long, Double>> assetPriceMap = requestPriceTickers(tickers, days);
        List<Map<String, Object>> portfolioHistoryData = getPortfolioHistoryData(pid, days, assetPriceMap);
        GetPortfolioBalanceResponse response = new GetPortfolioBalanceResponse();
        response.setPortfolioHistoryData(portfolioHistoryData);
        return response;
    }

    @GetMapping("/users/{id}/portfolios/balance")
    public GetUserOverallPortfolioBalanceResponse getUserOverallPortfolioBalance(@PathVariable String id,
            @RequestParam("duration") Integer days)
            throws Exception {
        List<Portfolio> portfolios = userService.findUserPortfolios(id);
        if (portfolios.isEmpty()) {
            throw new PortfolioNotFoundException();
        }
        List<String> tickers = getAssetTickerListByUserId(id);
        if (tickers.isEmpty()) {
            throw new PortfolioAssetNotFoundException();
        }
        Map<String, Map<Long, Double>> assetPriceMap = requestPriceTickers(tickers, days);
        List<Map<String, Object>> overallPortfolioBalanceData = new LinkedList<Map<String, Object>>();
        Map<String, Double> aggregatedMap = new LinkedHashMap<>();
        for (Portfolio portfolio : portfolios) {
            long pid = portfolio.getPid();
            List<Map<String, Object>> portfolioHistoryData = getPortfolioHistoryData(pid, days, assetPriceMap);
            for (Map<String, Object> portfolioHistoryDataElem : portfolioHistoryData) {
                String date = (String) portfolioHistoryDataElem.get("date");
                double balance = (double) portfolioHistoryDataElem.get("balance");
                aggregatedMap.put(date,
                        aggregatedMap.getOrDefault(date, 0.) + balance);
            }
        }
        for (Map.Entry<String, Double> entry : aggregatedMap.entrySet()) {
            Map<String, Object> aggregatedEntry = new HashMap<>();
            aggregatedEntry.put("date", entry.getKey());
            aggregatedEntry.put("balance", entry.getValue());
            overallPortfolioBalanceData.add(aggregatedEntry);
        }

        GetUserOverallPortfolioBalanceResponse response = new GetUserOverallPortfolioBalanceResponse();
        response.setOverallPortfolioHistoryData(overallPortfolioBalanceData);

        return response;
    }
}
