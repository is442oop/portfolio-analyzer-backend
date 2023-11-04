package com.backend.service.concretions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.model.Portfolio;
import com.backend.repository.PortfolioRepository;
import com.backend.exception.PortfolioNotFoundException;

@Service
public class PortfolioService implements com.backend.service.abstractions.IPortfolioService {
    private final PortfolioRepository repository;

    @Autowired
    public PortfolioService(PortfolioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Portfolio> findAll() {
        return repository.findAll();
    }

    @Override
    public Portfolio createNewPortfolio(Portfolio portfolio) {
        return repository.save(portfolio);
    }

    @Override
    public Portfolio findByPid(long pid) {
        Portfolio portfolio = repository.findByPid(pid);

        if (portfolio == null) {
            throw new PortfolioNotFoundException(pid);
        } else
            return portfolio;
    }

    @Override
    public Portfolio updatePortfolio(Portfolio updatedPortfolio) {
        return repository.save(updatedPortfolio);
    }

}
