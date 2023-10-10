package com.backend.service.concretions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.model.Portfolio;
import com.backend.repository.PortfolioRepository;


@Service
public class PortfolioService implements com.backend.service.abstractions.IPortfolioService {
    private final PortfolioRepository repository;

    @Autowired
    public PortfolioService(PortfolioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Portfolio createNewPortfolio(Portfolio portfolio){
        return repository.save(portfolio);
    }

    @Override
    public Portfolio findByPid(int pid){
        Portfolio portfolio = repository.findByPid(pid);

        if (portfolio == null){
            System.out.println("NO PORTFOLIO FOUND");
        }
        return portfolio;
    }
}
