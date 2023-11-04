package com.backend.controller;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.backend.model.Portfolio;
import com.backend.model.PortfolioAsset;
import com.backend.request.CreatePortfolioAssetRequest;
import com.backend.request.CreatePortfolioRequest;
import com.backend.request.UpdatePortfolioMetaDataRquest;
import com.backend.response.CreatePortfolioAssetResponse;
import com.backend.response.CreatePortfolioResponse;
import com.backend.response.FindAllPortfoliosResponse;
import com.backend.response.GetAllAssetsByPortfolioIdResponse;
import com.backend.response.GetPortfolioByIdResponse;
import com.backend.response.UpdatePortfolioMetadataResponse;
import com.backend.service.abstractions.IAssetService;
import com.backend.service.abstractions.IPortfolioAssetService;
import com.backend.service.abstractions.IPortfolioService;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class PortfolioController {
	Logger logger = LoggerFactory.getLogger(PortfolioController.class);
	private final IPortfolioService portfolioService;
	private final IPortfolioAssetService portfolioAssetService;
	private final IAssetService assetService;

	@Autowired
	public PortfolioController(IPortfolioService portfolioService, IPortfolioAssetService portfolioAssetService,
			IAssetService assetService) {
		this.portfolioService = portfolioService;
		this.portfolioAssetService = portfolioAssetService;
		this.assetService = assetService;
	}

	@GetMapping(path = "/portfolio")
	public FindAllPortfoliosResponse findAll() {
		List<Portfolio> portfolioList = portfolioService.findAll();

		FindAllPortfoliosResponse response = new FindAllPortfoliosResponse();
		response.setPortfolioList(portfolioList);
		return response;
	}

	@PostMapping(path = "/portfolio")
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
		System.out.println(portfolio.getPid());

		CreatePortfolioResponse response = new CreatePortfolioResponse();
		response.setPid(portfolio.getPid());
		response.setUserId(portfolio.getUserId());
		response.setPortfolioName(portfolio.getPortfolioName());
		response.setDescription(portfolio.getDescription());

		return response;
	}

	@GetMapping(path = "/portfolio/{pid}")
	public GetPortfolioByIdResponse getPortfolio(@PathVariable long pid) {
		Portfolio portfolio = portfolioService.findByPid(pid);

		GetPortfolioByIdResponse response = new GetPortfolioByIdResponse();
		response.setPid(portfolio.getPid());
		response.setUserId(portfolio.getUserId());
		response.setPortfolioName(portfolio.getPortfolioName());
		response.setDescription(portfolio.getDescription());

		return response;
	}

	@PutMapping(path = "/portfolio/{pid}")
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

	@PostMapping(path = "/portfolio/asset")
	public CreatePortfolioAssetResponse addPortfolioAsset(@RequestBody CreatePortfolioAssetRequest request) {
		if (request.getPortfolioId() == null) {
			throw new BadRequestException(Constants.MESSAGE_INVALIDPORTFOLIOID);
		}
		if (request.getAssetId() == null) {
			throw new BadRequestException(Constants.MESSAGE_INVALIDASSETID);
		}

		PortfolioAsset portfolioAsset = portfolioAssetService.createNewPortfolioAsset(
				new PortfolioAsset(
						request.getPortfolioId(),
						request.getAssetId(),
						request.getAveragePrice(),
						request.getQuantity()));

		java.util.Date time = new java.util.Date(portfolioAsset.getDateCreated() * 1000);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		CreatePortfolioAssetResponse response = new CreatePortfolioAssetResponse();

		logger.info("Created on: " + time);
		logger.info("New Portfolio Asset ID: " + request.getAssetId());
		logger.info("New Portfolio Asset Average Price: " + request.getAveragePrice());
		logger.info("New Portfolio Asset Quantity: " + request.getQuantity());

		response.setAssetId(portfolioAsset.getAssetId());
		response.setPortfolioId(portfolioAsset.getPortfolioId());
		response.setAveragePrice(portfolioAsset.getAveragePrice());
		response.setQuantity(portfolioAsset.getQuantity());
		response.setDateCreated(sdf.format(time));
		response.setDateModified(sdf.format(time));

		return response;
	}

	@GetMapping(path = "/portfolio/assets/{pid}")
	public GetAllAssetsByPortfolioIdResponse getAllAssetsByPortfolioId(@PathVariable int pid) {
		List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
		Map<Long, PortfolioAsset> aggregatedPortfolioAssets = portfolioAssetList.stream()
				.collect(Collectors.groupingBy(e -> e.getAssetId(), Collectors.collectingAndThen(
						Collectors.toList(),
						l -> l.stream().reduce(PortfolioAsset::merge).get())));

		GetAllAssetsByPortfolioIdResponse response = new GetAllAssetsByPortfolioIdResponse();
		response.setPortfolioAssetList(aggregatedPortfolioAssets.values().stream().collect(Collectors.toList()));
		return response;
	}

	@GetMapping(path = "/portfolio/{pid}/transactions")
	public List<Map<String, Object>> getTransactionsByPortfolioId(@PathVariable int pid) {
		List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
		List<Map<String, Object>> transactionList = new ArrayList<>();

		for (PortfolioAsset asset : portfolioAssetList) {
			Map<String, Object> transaction = new HashMap<>();
			transaction.put("portfolioAssetId", asset.getPortfolioAssetId());
			transaction.put("portfolioId", asset.getPortfolioId());
			transaction.put("assetId", asset.getAssetId());
			transaction.put("averagePrice", asset.getAveragePrice());
			transaction.put("quantity", asset.getQuantity());
			transaction.put("dateCreated", asset.getDateCreated().get("dateCreated"));
			transaction.put("dateModified", asset.getDateModified().get("dateModified"));
			transactionList.add(transaction);
		}
		return transactionList;
	}

	@GetMapping(path = "/portfolio/{pid}/allocation/industry")
	public List<Map<String, Object>> getAllocationPercentageByIndustry(@PathVariable long pid) {

		Portfolio portfolio = portfolioService.findByPid(pid);
		if (portfolio == null) {
			throw new PortfolioAssetNotFoundException(pid);
		}

		Map<String, Integer> industryMap = new HashMap<String, Integer>();
		List<PortfolioAsset> portfolioAssetList = portfolioAssetService.findAllByPortfolioId(pid);
		int totalSize = portfolioAssetList.size();

		for (int i = 0; i < totalSize; i++) {
			long portfolioAssetId = portfolioAssetList.get(i).getAssetId();
			Asset asset = assetService.findByAssetId(portfolioAssetId);
			String industry = asset.getAssetIndustry();

			if (!(industryMap.containsKey(industry))) {
				industryMap.put(industry, 1);
			} else {
				int count = industryMap.get(industry);
				count += 1;
				industryMap.put(industry, count);
			}
		}
		List<Map<String, Object>> allocationList = new ArrayList<>();

		for (Map.Entry<String, Integer> mapElement : industryMap.entrySet()) {
			String industry = mapElement.getKey();
			Integer count = mapElement.getValue();
			float percentage = (float) count / totalSize;
			percentage = Math.round(percentage * 100.0f) / 100.0f;

			Map<String, Object> allocation = new HashMap<>();
			allocation.put("stock", industry);
			allocation.put("percentage", percentage);
			allocationList.add(allocation);
		}

		return allocationList;

	}
}
