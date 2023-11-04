package com.backend.model;

import lombok.Data;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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

    @Column(name = "date_created")
    private long dateCreated;

    @Column(name = "date_modified")
    private long dateModified;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="asset_id", nullable = false, insertable=false, updatable=false)
    private Asset asset;

    public PortfolioAsset(){
    }

    public PortfolioAsset(long portfolioId, long assetId, double averagePrice, int quantity) {
        this.portfolioId = portfolioId;
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.assetId = assetId;
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

    public Map<String,String> getDateCreatedStringMap() {
        Map<String, String> dates = new HashMap<>();
        long unixCreated = this.dateCreated;
        Date created = new Date(unixCreated*1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String createdDate = jdf.format(created);

        dates.put("dateCreated", createdDate);

        return dates;
    }

    public Map<String,String> getDateModifiedStringMap() {
        Map<String, String> dates = new HashMap<>();
        long unixModified = this.dateModified;
        Date modified = new Date(unixModified*1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String modifiedDate = jdf.format(modified);

        dates.put("dateModified", modifiedDate);

        return dates;
    }
    
}
