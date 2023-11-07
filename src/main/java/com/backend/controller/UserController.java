package com.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.event.InternalFrameAdapter;

import com.backend.exception.BadCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
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
        logger.error("test");

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
                List<PortfolioAsset> aggregatedPortfolioAssetList = portfolioAssetService.aggregatePortfolioAssets(portfolioAssetList);
                listOfAggregatedPortfolioAssetList.add(aggregatedPortfolioAssetList);

            }
            Map<String, Integer> industryMap = new HashMap<>();
            int totalQuantity = 0;

            for(List<PortfolioAsset> aggregatedPortfolioAssetList : listOfAggregatedPortfolioAssetList) {
                for(PortfolioAsset aggregatedPortfolioAsset : aggregatedPortfolioAssetList) {
                    Asset asset = aggregatedPortfolioAsset.getAsset();
                    int aggregatedQuantity = aggregatedPortfolioAsset.getQuantity();
                    String industry = asset.getAssetIndustry();
                    totalQuantity += aggregatedQuantity;

                    if (!(industryMap.containsKey(industry))) {
                        industryMap.put(industry, aggregatedQuantity);
                    } else {
                        int updatedQuantity = industryMap.get(industry);
                        updatedQuantity += aggregatedQuantity;
                        industryMap.put(industry, updatedQuantity);
                    }
                }
            }

            System.out.println(industryMap);
		    List<Map<String, Object>> percentageByIndustry = new ArrayList<>();
		    for (Map.Entry<String, Integer> element : industryMap.entrySet()) {
			    String industry = element.getKey();
			    Integer quantity = element.getValue();
			    float percentage = (float) quantity / totalQuantity;

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
            List<PortfolioAsset> aggregatedPortfolioAssetList = portfolioAssetService.aggregatePortfolioAssets(portfolioAssetList);
            listOfAggregatedPortfolioAssetList.add(aggregatedPortfolioAssetList);

        }
        Map<String, Integer> tickerMap = new HashMap<>();
        int totalQuantity = 0;

         for(List<PortfolioAsset> aggregatedPortfolioAssetList : listOfAggregatedPortfolioAssetList) {
            for(PortfolioAsset aggregatedPortfolioAsset : aggregatedPortfolioAssetList) {
                String aggregatedPortfolioAssetTicker = aggregatedPortfolioAsset.getAssetTicker().trim();
                int aggregatedQuantity = aggregatedPortfolioAsset.getQuantity();
                totalQuantity += aggregatedQuantity;

                if (!(tickerMap.containsKey(aggregatedPortfolioAssetTicker))) {
                    tickerMap.put(aggregatedPortfolioAssetTicker, aggregatedQuantity);
                } else {
                    int updatedQuantity = tickerMap.get(aggregatedPortfolioAssetTicker);
                    updatedQuantity += aggregatedQuantity;
                    tickerMap.put(aggregatedPortfolioAssetTicker, updatedQuantity);
                }
            }
        }

        List<Map<String, Object>> percentageByTickerList = new ArrayList<>();

		for (Map.Entry<String, Integer> element : tickerMap.entrySet()) {
			String assetTicker = element.getKey();
			Integer quantity = element.getValue();
			float percentage = (float) quantity / totalQuantity;

			Map<String, Object> allocation = new HashMap<>();
            allocation.put("assetTicker", assetTicker);
            allocation.put("percentage", percentage);
            percentageByTickerList.add(allocation);
		}

		return percentageByTickerList;



    }

}
