package com.backend.service.abstractions;

import com.backend.model.Portfolio;

public interface IPortfolioService {
    Portfolio createNewPortfolio(Portfolio portfolio);
    Portfolio findByPid(int pid);
}
