package com.backend.controllers;

import com.backend.resources.Portfolio;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController

public class PortfolioController {
    @GetMapping(path = "/test")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@PostMapping(path = "/portfolio/create")
	public JSONObject createPortfolio(@RequestBody Portfolio portfolio) {
		System.out.println(portfolio.toJson());
		return portfolio.toJson();
	}

	@GetMapping(path = "/portfolio/{id}")
	public String getPortfolio(@PathVariable int id) {
		return "Portfolio ID : " + id;
	}
}
