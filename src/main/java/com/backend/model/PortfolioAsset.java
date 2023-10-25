package com.backend.model;

import lombok.Data;

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
@Table(name="portfolio_asset", schema = "prod")
@SequenceGenerator(name = "portfolio-asset-sequence-gen", sequenceName = "portfolio_asset_id_seq" , allocationSize = 1)

public class PortfolioAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portfolio-asset-sequence-gen")
    @Column(name = "portfolio_asset_id")
    private long portfolioAssetId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="portfolio_id", nullable = false, insertable=false, updatable=false)
    private Portfolio portfolio;

    @Column(name = "portfolio_id")
    private long portfolioId;

    @Column(name = "asset_id")
    private long assetId;

    @Column(name = "average_price_decimal")
    private double averagePrice;

    @Column(name = "quantity")
    private int quantity;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="asset_id", nullable = false, insertable=false, updatable=false)
    private Asset asset;

    protected PortfolioAsset(){
    }

    public PortfolioAsset(long portfolioId, long assetId, double averagePrice, int quantity) {
        this.portfolioId = portfolioId;
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.assetId = assetId;
    }
    
}
