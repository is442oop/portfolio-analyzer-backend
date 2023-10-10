package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.backend.model.Portfolio;

@RepositoryRestResource(exported = false)
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Portfolio findByPid(int pid);
    Portfolio findAllById(int id);
}
