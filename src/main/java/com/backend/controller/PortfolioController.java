package com.backend.controller;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.backend.model.Asset;
import com.backend.configuration.Constants;
import com.backend.exception.BadRequestException;
import com.backend.exception.PortfolioAssetNotFoundException;
import com.backend.exception.PortfolioNotFoundException;
import com.backend.model.Portfolio;
import com.backend.model.PortfolioAsset;
import com.backend.request.CreatePortfolioAssetRequest;
import com.backend.request.CreatePortfolioRequest;
import com.backend.request.UpdatePortfolioMetaDataRquest;
import com.backend.response.CreatePortfolioAssetResponse;
import com.backend.response.CreatePortfolioResponse;
import com.backend.response.DeletePortfolioResponse;
import com.backend.response.FindAllPortfoliosResponse;
import com.backend.response.GetAllAssetsByPortfolioIdResponse;
import com.backend.response.GetAllPortfolioAssetsByUserResponse;
import com.backend.response.GetPortfolioAssetResponse;
import com.backend.response.GetPortfolioByIdResponse;
import com.backend.response.UpdatePortfolioMetadataResponse;
import com.backend.service.abstractions.IAssetService;
import com.backend.service.abstractions.IPortfolioAssetService;
import com.backend.service.abstractions.IPortfolioService;
import com.backend.service.abstractions.IUserService;
import com.backend.request.DeletePortfolioAssetRequest;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class PortfolioController {
    Logger logger = LoggerFactory.getLogger(PortfolioController.class);
    private final IPortfolioService portfolioService;
    private final IPortfolioAssetService portfolioAssetService;
    private final IUserService userService;

    @Autowired
    public PortfolioController(IPortfolioService portfolioService, IPortfolioAssetService portfolioAssetService,
            IUserService userService) {
        this.portfolioService = portfolioService;
        this.portfolioAssetService = portfolioAssetService;
        this.userService = userService;
    }

    @GetMapping(path = "/portfolios")
    public FindAllPortfoliosResponse findAll() {
        List<Portfolio> portfolioList = portfolioService.findAll();

        FindAllPortfoliosResponse response = new FindAllPortfoliosResponse();
        response.setPortfolioList(portfolioList);
        return response;
    }

    @PostMapping(path = "/portfolios")
    public CreatePortfolioResponse createPortfolio(@RequestBody CreatePortfolioRequest request) {
        if (request.getPortfolioName() == null || request.getPortfolioName().isEmpty()) {
            throw new BadRequestException(Constants.MESSAGE_MISSINGPORTFOLIONAME);
        }
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            throw new BadRequestException(Constants.MESSAGE_MISSINGPORTFOLIODESC);
        }

        logger.info("Beginning creation of new portfolio with the following details");
        logger.info("New Portfolio Name: " + request.getPortfolioName());
        logger.info("New Portfolio Description: " + request.getDescription());
        Portfolio portfolio = portfolioService.createNewPortfolio(
                new Portfolio(
                        request.getUserId(),
                        request.getPortfolioName(),
                        request.getDescription()));

        CreatePortfolioResponse response = new CreatePortfolioResponse();
        response.setPid(portfolio.getPid());
        response.setUserId(portfolio.getUserId());
        response.setPortfolioName(portfolio.getPortfolioName());
        response.setDescription(portfolio.getDescription());

        return response;
    }

    @GetMapping(path = "/portfolios/{pid}")
    public GetPortfolioByIdResponse getPortfolio(@PathVariable long pid) {
        Portfolio portfolio = portfolioService.findByPid(pid);

        GetPortfolioByIdResponse response = new GetPortfolioByIdResponse();
        response.setPid(portfolio.getPid());
        response.setUserId(portfolio.getUserId());
        response.setPortfolioName(portfolio.getPortfolioName());
        response.setDescription(portfolio.getDescription());

        return response;
    }

    @PutMapping(path = "/portfolios/{pid}")
    public UpdatePortfolioMetadataResponse updatePortfolio(@PathVariable long pid,
            @RequestBody UpdatePortfolioMetaDataRquest body) {
        Portfolio portfolio = portfolioService.findByPid(pid);

        if (portfolio == null) {
            throw new PortfolioAssetNotFoundException(pid);
        }

        logger.info("Beginning update of portfolio with the following details");
        logger.info("Portfolio ID: " + pid);
        logger.info("New Portfolio Name: " + body.getPortfolioName());
        logger.info("New Portfolio Description: " + body.getDescription());

        String portfolioName = body.getPortfolioName();
        String description = body.getDescription();

        if (portfolioName == null && description == null) {
            throw new BadRequestException(Constants.MESSAGE_MISSINGPORTFOLIOMETADATA);
        }
        if (portfolioName != null && !portfolioName.isEmpty()) {
            portfolio.setPortfolioName(portfolioName);
        }
        if (description != null && !description.isEmpty()) {
            portfolio.setDescription(description);
        }

        portfolioService.updatePortfolio(portfolio);

        logger.info("Portfolio updated successfully");

        UpdatePortfolioMetadataResponse response = new UpdatePortfolioMetadataResponse();
        response.setPortfolioName(portfolio.getPortfolioName());
        response.setDescription(portfolio.getDescription());

        return response;
    }

    @PostMapping(path = "/portfolios/assets")
    public CreatePortfolioAssetResponse addPortfolioAsset(@RequestBody CreatePortfolioAssetRequest request) {
        if (request.getPortfolioId() == null) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDPORTFOLIOID);
        }
        if (request.getAssetTicker() == null) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDASSETTICKER);
        }

        logger.info("New Portfolio Asset Ticker: " + request.getAssetTicker());
        logger.info("New Portfolio Asset Average Price: " + request.getPrice());
        logger.info("New Portfolio Asset Quantity: " + request.getQuantity());
        logger.info("New Portfolio Asset Created At: " + request.getDateCreated());
        PortfolioAsset portfolioAsset;
        try {
            portfolioAsset = portfolioAssetService.createNewPortfolioAsset(
                    new PortfolioAsset(
                            request.getPortfolioId(),
                            request.getAssetTicker(),
                            request.getPrice(),
                            request.getQuantity(),
                            request.getDateCreated()));
        } catch (Exception e) {
            throw new BadRequestException(Constants.MESSAGE_MALFORMEDREQUEST);
        }
        java.util.Date time = new java.util.Date(portfolioAsset.getDateCreated() * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'+08:00'");
        CreatePortfolioAssetResponse response = new CreatePortfolioAssetResponse();
        logger.info("New Portfolio Asset Average Price: " + request.getPrice());
        logger.info("New Portfolio Asset Quantity: " + request.getQuantity());

        response.setAssetTicker(portfolioAsset.getAssetTicker());
        response.setPortfolioId(portfolioAsset.getPortfolioId());
        response.setPrice(portfolioAsset.getPrice());
        response.setQuantity(portfolioAsset.getQuantity());
        response.setDateCreated(sdf.format(time));
        response.setDateModified(sdf.format(time));

        return response;
    }

    @DeleteMapping(path = "/portfolios/assets")
    public void deletePortfolioAsset(@RequestBody DeletePortfolioAssetRequest request) {
        if (request.getPortfolioId() == null) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDPORTFOLIOID);
        }
        if (request.getAssetTicker() == null) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDASSETTICKER);
        }

        long pid = request.getPortfolioId();
        String assetTicker = request.getAssetTicker();

        List<PortfolioAsset> portfolioAssets = portfolioAssetService.findByPortfolioIdAndAssetTicker(pid, assetTicker);

        for (PortfolioAsset portfolioAsset : portfolioAssets) {
            if (portfolioAsset == null) {
                throw new PortfolioAssetNotFoundException(pid, assetTicker);
            }
            portfolioAssetService.deletePortfolioAsset(pid, assetTicker);
        }
    }

    @GetMapping(path = "/portfolios/{pid}/assets")
    public GetAllAssetsByPortfolioIdResponse getAllAssetsByPortfolioId(@PathVariable long pid) {
        List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
        if (portfolioAssetList.isEmpty()) {
            // here
            throw new PortfolioNotFoundException();
        }

        Map<String, PortfolioAsset> aggregatedPortfolioAssets = portfolioAssetList.stream()
                .collect(Collectors.groupingBy(e -> e.getAssetTicker(), Collectors.collectingAndThen(
                        Collectors.toList(),
                        l -> l.stream().reduce(PortfolioAsset::merge).get())));

        GetAllAssetsByPortfolioIdResponse response = new GetAllAssetsByPortfolioIdResponse();
        response.setPortfolioAssetList(aggregatedPortfolioAssets.values().stream().collect(Collectors.toList()));
        return response;
    }

    @GetMapping(path = "/portfolios/{pid}/assets/{assetTicker}")
    public List<PortfolioAsset> getPortfolioAssetByPortfolioIdAndAssetTicker(@PathVariable long pid,
            @PathVariable String assetTicker) {

        List<PortfolioAsset> portfolioAsset = portfolioAssetService.findByPortfolioIdAndAssetTicker(pid, assetTicker);
        if (portfolioAsset == null) {
            throw new PortfolioAssetNotFoundException(pid, assetTicker);
        }

        return portfolioAsset;
    }

    @GetMapping(path = "/portfolios/{pid}/transactions")
    public List<Map<String, Object>> getTransactionsByPortfolioId(@PathVariable long pid) {
        List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
        List<Map<String, Object>> transactionList = new ArrayList<>();

        for (PortfolioAsset asset : portfolioAssetList) {
            Map<String, Object> transaction = new HashMap<>();
            transaction.put("portfolioAssetId", asset.getPortfolioAssetId());
            transaction.put("portfolioId", asset.getPortfolioId());
            transaction.put("assetTicker", asset.getAssetTicker());
            transaction.put("price", asset.getPrice());
            transaction.put("quantity", asset.getQuantity());
            transaction.put("dateCreated", asset.getDateCreatedStringMap().get("dateCreated"));
            transaction.put("dateModified", asset.getDateModifiedStringMap().get("dateModified"));
            transactionList.add(transaction);
        }
        return transactionList;
    }

    @GetMapping(path = "/portfolios/{pid}/allocation/ticker")
    public List<Map<String, Object>> getAllocationPercentageByTicker(@PathVariable long pid) {
        Portfolio portfolio = portfolioService.findByPid(pid);
        if (portfolio == null) {
            throw new PortfolioNotFoundException(pid);
        }

        List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
        if (portfolioAssetList.isEmpty()) {
            throw new PortfolioAssetNotFoundException();
        }

        List<PortfolioAsset> aggregatedPortfolioAssetList = portfolioAssetService
                .aggregatePortfolioAssets(portfolioAssetList);
        Map<String, Double> tickerMap = new HashMap<>();
        double totalAmount = 0.0;

        for (PortfolioAsset aggregatedPortfolioAsset : aggregatedPortfolioAssetList) {
            String aggregatedPortfolioAssetTicker = aggregatedPortfolioAsset.getAssetTicker().trim();
            double amount = aggregatedPortfolioAsset.getPrice();
            int quantity = aggregatedPortfolioAsset.getQuantity();
            double aggregatedAmount = amount * quantity;
            totalAmount += aggregatedAmount;

            logger.info("Asset Ticker = " + aggregatedPortfolioAssetTicker + ", Amount = " + aggregatedAmount);

            tickerMap.put(aggregatedPortfolioAssetTicker, aggregatedAmount);
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

    @GetMapping(path = "/portfolios/{pid}/allocation/industry")
    public List<Map<String, Object>> getAllocationPercentageByIndustry(@PathVariable long pid) {
        Portfolio portfolio = portfolioService.findByPid(pid);
        if (portfolio == null) {
            throw new PortfolioNotFoundException(pid);
        }

        List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
        if (portfolioAssetList.isEmpty()) {
            throw new PortfolioAssetNotFoundException();
        }

        List<PortfolioAsset> aggregatedPortfolioAssetList = portfolioAssetService
                .aggregatePortfolioAssets(portfolioAssetList);
        Map<String, Double> industryMap = new HashMap<>();
        double totalAmount = 0.0;

        for (PortfolioAsset aggregatedPortfolioAsset : aggregatedPortfolioAssetList) {
            Asset asset = aggregatedPortfolioAsset.getAsset();
            double amount = aggregatedPortfolioAsset.getPrice();
            int quantity = aggregatedPortfolioAsset.getQuantity();
            double aggregatedAmount = amount * quantity;
            String industry = asset.getAssetIndustry();

            logger.info("Industry = " + industry + ", Amount = " + aggregatedAmount);

            totalAmount += aggregatedAmount;

            if (!(industryMap.containsKey(industry))) {
                industryMap.put(industry, aggregatedAmount);
            } else {
                double updatedAmount = industryMap.get(industry);
                updatedAmount += aggregatedAmount;
                industryMap.put(industry, updatedAmount);
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

    @GetMapping(path = "/portfolio/asset/user/{userId}")
    public GetAllPortfolioAssetsByUserResponse getAllAssetsByUserId(@PathVariable String userId) {
        List<PortfolioAsset> output = new ArrayList<>();
        List<Portfolio> portfolioList = userService.findUserPortfolios(userId);

        for (Portfolio portfolio : portfolioList) {
            long pid = portfolio.getPid();
            List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
            for (PortfolioAsset portfolioAsset : portfolioAssetList) {
                output.add(portfolioAsset);
            }
        }
        GetAllPortfolioAssetsByUserResponse response = new GetAllPortfolioAssetsByUserResponse();
        response.setPortfolioAssetList(output);
        return response;
    }

    @DeleteMapping(path = "/portfolios/{pid}")
    public DeletePortfolioResponse deletePortfolio(@PathVariable long pid) {
        // check if pid is valid
        Portfolio portfolio = portfolioService.findByPid(pid);
        if (portfolio == null) {
            throw new PortfolioNotFoundException(pid);
        }
        portfolioService.deletePortfolio(pid);
        DeletePortfolioResponse response = new DeletePortfolioResponse();
        response.setMessage("Portfolio with pid=" + pid + " deleted successfully");

        return response;
    }

    @GetMapping(path = "/portfolios/assets/{portfolioAssetId}")
    public GetPortfolioAssetResponse getPortfolioAssetByPortfolioAssetId(@PathVariable long portfolioAssetId) {
        PortfolioAsset portfolioAsset = portfolioAssetService.findByPortfolioAssetId(portfolioAssetId);
        if (portfolioAsset == null) {
            throw new PortfolioAssetNotFoundException(portfolioAssetId);
        }
        GetPortfolioAssetResponse response = new GetPortfolioAssetResponse();
        response.setPortfolioAsset(portfolioAsset);

        return response;
    }

}
