package com.backend.controller;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.net.HttpURLConnection;
// import java.time.LocalDate;
// import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
// import java.net.URL;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

// import com.backend.response.DailyTimeAdjustedResponse;
// import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class PortfolioBalanceController {

    public static LinkedHashMap<String, Float> getHistoricalPrice(String ticker, String key, int days) throws Exception{
        HttpClient httpClient = HttpClients.createDefault();
        String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + ticker + "&apikey=" + key;
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

        LinkedHashMap<String, Float> output = new LinkedHashMap<>();

        int counter = 0;
        while (dateIterator.hasNext()) {
            counter += 1;
            if (counter>days){
                break;
            }
            String dateClose = dateIterator.next();
            JsonObject priceDate = histPrices.getAsJsonObject(dateClose);
            float priceString = priceDate.get("5. adjusted close").getAsFloat();
            output.put(dateClose, priceString);
        }
        return output;
    }


    @GetMapping(path = "/portfolio_balance") // need to make a request class
    public void getPortfolioBalance(@RequestParam("duration") String duration) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10); // or any other number that makes sense for your use case
        List<String> tickers = Arrays.asList("AAPL", "IBM", "TSLA", "GOOG", "META", "AMZN", "NVDA", "AMD", "AAPL", "IBM", "TSLA", "GOOG", "META", "AMZN", "NVDA", "AMD");
        List<Future<LinkedHashMap<String, Float>>> futures = new ArrayList<>();
        int durationInt = Integer.parseInt(duration);
        for (String ticker : tickers) {
            Callable<LinkedHashMap<String, Float>> callableTask = () -> {
                return getHistoricalPrice(ticker, "AJ3EONYVGOHPWZPC", durationInt);
            };
            Future<LinkedHashMap<String, Float>> future = executorService.submit(callableTask);
            futures.add(future);
        }

        HashMap<String, Float> total = new HashMap<>();
        for (Future<LinkedHashMap<String, Float>> future : futures) {
            LinkedHashMap<String, Float> tickerHistPrices = future.get();
            System.out.println(tickerHistPrices);

        }

        executorService.shutdown();
    }
}
