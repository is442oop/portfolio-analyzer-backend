package com.backend.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



import com.backend.model.Portfolio;
import com.backend.model.PortfolioAsset;
import com.backend.configuration.Constants;
import com.backend.exception.BadRequestException;
import com.backend.request.CreatePortfolioRequest;
import com.backend.request.CreatePortfolioAssetRequest;
import com.backend.response.CreatePortfolioResponse;
import com.backend.response.GetPortfolioByIdResponse;
import com.backend.response.FindAllPortfoliosResponse;
import com.backend.response.CreatePortfolioAssetResponse;
import com.backend.service.abstractions.IPortfolioService;
import com.backend.service.abstractions.IPortfolioAssetService;



@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class PortfolioController {
	Logger logger = LoggerFactory.getLogger(PortfolioController.class);
	private final IPortfolioService portfolioService;
	private final IPortfolioAssetService portfolioAssetService;


    @Autowired
    public PortfolioController(IPortfolioService portfolioService, IPortfolioAssetService portfolioAssetService) {
		this.portfolioService = portfolioService;
		this.portfolioAssetService = portfolioAssetService;
    }

	@GetMapping(path = "/portfolio")
	public FindAllPortfoliosResponse findAll(){
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
				request.getDescription()
			)
		);
		System.out.println(portfolio.getPid());


		CreatePortfolioResponse response = new CreatePortfolioResponse();
		response.setPid(portfolio.getPid());
		response.setUserId(portfolio.getUserId());
		response.setPortfolioName(portfolio.getPortfolioName());
		response.setDescription(portfolio.getDescription());

		return response;
	}

	@GetMapping(path = "/portfolio/{pid}")
	public GetPortfolioByIdResponse getPortfolio(@PathVariable int pid) {
		Portfolio portfolio = portfolioService.findByPid(pid);

		GetPortfolioByIdResponse response = new GetPortfolioByIdResponse();
		response.setPid(portfolio.getPid());
		response.setUserId(portfolio.getUserId());
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

		// Portfolio portfolio = restTemplate.exchange("http://localhost:8080/portfolio/"+request.getPortfolioId(), HttpMethod.GET, null, Portfolio.class).getBody();

		
		// logger.info("Adding " + request.getAssetId() + " to portfolio " + portfolio.getPid());
		
		PortfolioAsset portfolioAsset = portfolioAssetService.createNewPortfolioAsset(
			new PortfolioAsset(
				request.getPortfolioId(),
				request.getAssetId(),
				request.getAveragePrice(),
				request.getQuantity()	
			)
		);
		
		java.util.Date time = new java.util.Date(portfolioAsset.getDateCreated()*1000);
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
}
