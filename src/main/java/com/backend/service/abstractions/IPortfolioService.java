package com.backend.service.abstractions;

import java.util.List;

import com.backend.model.Portfolio;

public interface IPortfolioService {
    List<Portfolio> findAll();
    Portfolio createNewPortfolio(Portfolio portfolio);
    Portfolio findByPid(long pid);
}
