package com.backend.model;

import lombok.Data;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    // private long userId;
    private User user;

    @Column(name = "user_id", insertable=false, updatable=false)
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

    // public long getUserId() {
    //     return this.user.getId();
    // }

}
