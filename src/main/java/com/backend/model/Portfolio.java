package com.backend.model;

import lombok.Data;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private long id;
    private String portfolioName;
    private String description;
    private LocalDate date;

    protected Portfolio(){
    }

    public Portfolio(long id, String portfolioName, String description, LocalDate date) {
        this.id = id;
        this.portfolioName = portfolioName;
        this.description = description;
        this.date = date;
    }
}
