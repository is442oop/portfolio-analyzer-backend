package com.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.backend.exception.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.configuration.Constants;
import com.backend.exception.BadRequestException;
import com.backend.exception.PortfolioNotFoundException;
import com.backend.model.Asset;
import com.backend.model.Portfolio;
import com.backend.model.PortfolioAsset;
import com.backend.model.User;
import com.backend.request.CreateUserRequest;
import com.backend.response.CreateUserResponse;
import com.backend.response.FindAllUsersResponse;
import com.backend.response.FindUserPortfolios;
import com.backend.response.FindUserResponse;
import com.backend.service.abstractions.IPortfolioAssetService;
import com.backend.service.abstractions.IUserService;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;
    private final IPortfolioAssetService portfolioAssetService;

    @Autowired
    public UserController(IUserService userService, IPortfolioAssetService portfolioAssetService) {
        this.userService = userService;
        this.portfolioAssetService = portfolioAssetService;
    }

    @GetMapping("/users")
    public FindAllUsersResponse findAll() {
        List<User> userList = userService.findAll();

        FindAllUsersResponse response = new FindAllUsersResponse();
        response.setUserList(userList);
        return response;
    }

    @GetMapping("/users/{id}")
    public FindUserResponse findById(@PathVariable String id) {
        User user = userService.findById(id);
        FindUserResponse response = new FindUserResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setPortfolioList(user.getPortfolios());

        return response;
    }

    @PostMapping("/users")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) {
        logger.info("username: " + request.getUsername());
        logger.info("email: " + request.getEmail());

        if (request.getId() == null) {
            throw new BadRequestException(Constants.MESSAGE_MALFORMEDREQUEST);
        }

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDUSERNAME);
        }

        if (request.getEmail() == null
                || !Pattern.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", request.getEmail())) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDEMAIL);
        }
        boolean isUserIdExist = userService.isUserIdExist(request.getId());
        if (isUserIdExist) {
            throw new BadCredentialsException(Constants.MESSAGE_SAMEUSEREXIST);
        }

        boolean isUsernameExist = userService.isUsernameExist(request.getUsername());
        if (isUsernameExist) {
            throw new BadCredentialsException(Constants.MESSAGE_SAMEUSERNAMEEXIST);
        }

        boolean isEmailExist = userService.isEmailExist(request.getEmail());
        if (isEmailExist) {
            throw new BadCredentialsException(Constants.MESSAGE_SAMEEMAILEXIST);
        }

        User user = userService.createNewUser(new User(request.getId(), request.getUsername(), request.getEmail()));

        CreateUserResponse response = new CreateUserResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }

    @GetMapping("/users/{id}/portfolios")
    public FindUserPortfolios findUserPortolios(@PathVariable String id) {
        logger.info("Finding user " + id + " portfolio");
        List<Portfolio> portfolios = userService.findUserPortfolios(id);
        FindUserPortfolios response = new FindUserPortfolios();

        response.setPortfolioList(portfolios);

        return response;

    }

    @GetMapping("/users/{id}/portfolios/allocation/industry")
    public List<Map<String, Object>> getAllocationAcrossUserPortfolios(@PathVariable String id) {
        logger.info("Finding user " + id + " portfolio");
        List<Portfolio> portfolios = userService.findUserPortfolios(id);
        if (portfolios.isEmpty()) {
            throw new PortfolioNotFoundException();
        }

        List<List<PortfolioAsset>> listOfAggregatedPortfolioAssetList = new ArrayList<>();

        for (Portfolio portfolio : portfolios) {
            long pid = portfolio.getPid();
            List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
            List<PortfolioAsset> aggregatedPortfolioAssetList = portfolioAssetService
                    .aggregatePortfolioAssets(portfolioAssetList);
            listOfAggregatedPortfolioAssetList.add(aggregatedPortfolioAssetList);

        }

        Map<String, Double> industryMap = new HashMap<>();
        double totalAmount = 0;

        for (List<PortfolioAsset> aggregatedPortfolioAssetList : listOfAggregatedPortfolioAssetList) {
            for (PortfolioAsset aggregatedPortfolioAsset : aggregatedPortfolioAssetList) {
                Asset asset = aggregatedPortfolioAsset.getAsset();
                double price = aggregatedPortfolioAsset.getPrice();
                int quantity = aggregatedPortfolioAsset.getQuantity();
                String industry = asset.getAssetIndustry();
                double aggregatedAmount = price * quantity;
                totalAmount += aggregatedAmount;

                logger.info("Industry = " + industry + ", Amount = " + aggregatedAmount);

                if (!(industryMap.containsKey(industry))) {
                    industryMap.put(industry, aggregatedAmount);
                } else {
                    double updatedAmount = industryMap.get(industry);
                    updatedAmount += aggregatedAmount;
                    industryMap.put(industry, updatedAmount);
                }
            }
        }
        logger.info("Total amount in portfolio= " + totalAmount);
        logger.info("Amount of assets in ticker map {}", industryMap);

        List<Map<String, Object>> percentageByIndustry = new ArrayList<>();

        for (Map.Entry<String, Double> element : industryMap.entrySet()) {
            String industry = element.getKey();
            double amount = element.getValue();
            double percentage = amount / totalAmount;

            Map<String, Object> allocation = new HashMap<>();
            allocation.put("industry", industry);
            allocation.put("percentage", percentage);
            percentageByIndustry.add(allocation);
        }

        return percentageByIndustry;

    }

    @GetMapping("/users/{id}/portfolios/allocation/ticker")
    public List<Map<String, Object>> getAllocationPercentageByTicker(@PathVariable String id) {
        logger.info("Finding user " + id + " portfolio");
        List<Portfolio> portfolios = userService.findUserPortfolios(id);
        if (portfolios.isEmpty()) {
            throw new PortfolioNotFoundException();
        }

        List<List<PortfolioAsset>> listOfAggregatedPortfolioAssetList = new ArrayList<>();

        for (Portfolio portfolio : portfolios) {
            long pid = portfolio.getPid();
            List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
            List<PortfolioAsset> aggregatedPortfolioAssetList = portfolioAssetService
                    .aggregatePortfolioAssets(portfolioAssetList);
            listOfAggregatedPortfolioAssetList.add(aggregatedPortfolioAssetList);

        }
        Map<String, Double> tickerMap = new HashMap<>();
        double totalAmount = 0.0;

        for (List<PortfolioAsset> aggregatedPortfolioAssetList : listOfAggregatedPortfolioAssetList) {
            for (PortfolioAsset aggregatedPortfolioAsset : aggregatedPortfolioAssetList) {
                String aggregatedPortfolioAssetTicker = aggregatedPortfolioAsset.getAssetTicker().trim();
                double price = aggregatedPortfolioAsset.getPrice();
                int quantity = aggregatedPortfolioAsset.getQuantity();
                double aggregatedAmount = price * quantity;
                totalAmount += aggregatedAmount;

                logger.info("Asset Ticker = " + aggregatedPortfolioAssetTicker + ", Amount = " + aggregatedAmount);

                if (!(tickerMap.containsKey(aggregatedPortfolioAssetTicker))) {
                    tickerMap.put(aggregatedPortfolioAssetTicker, aggregatedAmount);
                } else {
                    double updatedAmount = tickerMap.get(aggregatedPortfolioAssetTicker);
                    updatedAmount += aggregatedAmount;
                    tickerMap.put(aggregatedPortfolioAssetTicker, updatedAmount);
                }
            }
        }
        logger.info("Total amount in portfolio= " + totalAmount);
        logger.info("Amount of assets in ticker map {}", tickerMap);

        List<Map<String, Object>> percentageByTickerList = new ArrayList<>();

        for (Map.Entry<String, Double> element : tickerMap.entrySet()) {
            String assetTicker = element.getKey();
            double amount = element.getValue();
            double percentage = amount / totalAmount;

            Map<String, Object> allocation = new HashMap<>();
            allocation.put("assetTicker", assetTicker);
            allocation.put("percentage", percentage);
            percentageByTickerList.add(allocation);
        }

        return percentageByTickerList;

    }
}
