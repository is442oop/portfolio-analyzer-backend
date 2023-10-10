package com.backend.model;

import lombok.Data;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;

@Data
@Entity
@Table(name="portfolio", schema = "prod")
@SequenceGenerator(name = "portfolio-sequence-gen", sequenceName = "portfolio_id_seq" , allocationSize = 1)
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portfolio-sequence-gen")
    private long pid;
    
    @Column(name = "user_id")
    private long userId;

    @Column(name = "portfolio_name")
    private String portfolioName;
    private String description;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    protected Portfolio(){
    }

    public Portfolio(long userId, String portfolioName, String description, LocalDate creationDate) {
        this.userId = userId;
        this.portfolioName = portfolioName;
        this.description = description;
        this.creationDate = creationDate;
    }
}
