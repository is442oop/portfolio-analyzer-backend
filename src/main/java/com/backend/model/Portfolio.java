package com.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "portfolio", schema = "prod")
@SequenceGenerator(name = "portfolio-sequence-gen", sequenceName = "portfolio_id_seq", allocationSize = 1)
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portfolio-sequence-gen")
    private long pid;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    private String userId;


    @Column(name = "portfolio_name")
    private String portfolioName;
    private String description;

    protected Portfolio() {
    }

    public Portfolio(String userId, String portfolioName, String description) {
        this.userId = userId;
        this.portfolioName = portfolioName;
        this.description = description;
    }


}
