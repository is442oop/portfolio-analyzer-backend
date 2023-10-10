package com.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.backend.model.Portfolio;
import com.backend.configuration.Constants;
import com.backend.exception.BadRequestException;
import com.backend.request.CreatePortfolioRequest;
import com.backend.response.CreatePortfolioResponse;
import com.backend.response.GetPortfolioByIdResponse;
import com.backend.service.abstractions.IPortfolioService;


@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class PortfolioController {
	Logger logger = LoggerFactory.getLogger(PortfolioController.class);
	private final IPortfolioService portfolioService;

    @Autowired
    public PortfolioController(IPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

	@PostMapping(path = "/portfolio")
	public CreatePortfolioResponse createPortfolio(@RequestBody CreatePortfolioRequest request) {
		logger.info("Creating new portfolio with the following details");
		logger.info("Portfolio Name: " + request.getPortfolioName());
        logger.info("Portfolio Description: " + request.getDescription());
        Portfolio portfolio = portfolioService.createNewPortfolio(
			new Portfolio(
				request.getId(),
				request.getPortfolioName(),
				request.getDescription(),
				request.getDate()
			)
		);
		System.out.println(portfolio.getPid());

		CreatePortfolioResponse response = new CreatePortfolioResponse();
		response.setPid(portfolio.getPid());
		response.setId(portfolio.getId());
		response.setPortfolioName(portfolio.getPortfolioName());
		response.setDescription(portfolio.getDescription());
		response.setDate(portfolio.getDate());

		return response;
	}

	@GetMapping(path = "/portfolio/{pid}")
	public GetPortfolioByIdResponse getPortfolio(@PathVariable int pid) {
		Portfolio portfolio = portfolioService.findByPid(pid);

		GetPortfolioByIdResponse response = new GetPortfolioByIdResponse();
		response.setPid(portfolio.getPid());
		response.setId(portfolio.getId());
		response.setPortfolioName(portfolio.getPortfolioName());
		response.setDescription(portfolio.getDescription());
		response.setDate(portfolio.getDate());

		return response;
	}
}
