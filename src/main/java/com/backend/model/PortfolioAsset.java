package com.backend.model;

import lombok.Data;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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

    @Column(name = "asset_ticker")
    private String assetTicker;

    @Column(name = "average_price_decimal")
    private double averagePrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "date_created")
    private long dateCreated;

    @Column(name = "date_modified")
    private long dateModified;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="asset_ticker", nullable = false, insertable=false, updatable=false)
    private Asset asset;

    public PortfolioAsset(){
    }

    public PortfolioAsset(long portfolioId, String assetTicker, double averagePrice, int quantity) {
        this.portfolioId = portfolioId;
        this.assetTicker = assetTicker;
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.dateCreated = System.currentTimeMillis() / 1000;
        this.dateModified = System.currentTimeMillis() / 1000;
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public PortfolioAsset merge(PortfolioAsset other) {
        double thisAggregatePrice = this.averagePrice * this.quantity;
        double otherAggregatePrice = other.getAveragePrice() * other.getQuantity();
        this.quantity += other.getQuantity();
        this.averagePrice = (thisAggregatePrice + otherAggregatePrice) / this.quantity;
        this.averagePrice = Math.round(this.averagePrice * 100.0) / 100.0;
        
        return this;
    }
    
}
