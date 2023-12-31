package com.backend.model;

import lombok.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@Table(name = "portfolio_asset", schema = "prod")
@SequenceGenerator(name = "portfolio-asset-sequence-gen", sequenceName = "portfolio_asset_id_seq", allocationSize = 1)

public class PortfolioAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "portfolio-asset-sequence-gen")
    @Column(name = "portfolio_asset_id")
    private long portfolioAssetId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false, insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Portfolio portfolio;

    @Column(name = "portfolio_id")
    private long portfolioId;

    @Column(name = "asset_ticker")
    private String assetTicker;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "date_created")
    private long dateCreated;

    @Column(name = "date_modified")
    private long dateModified;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_ticker", nullable = false, insertable = false, updatable = false)
    private Asset asset;

    public PortfolioAsset() {
    }

    public PortfolioAsset(long portfolioId, String assetTicker, double price, int quantity, long dateCreated) {
        this.portfolioId = portfolioId;
        this.assetTicker = assetTicker;
        this.price = price;
        this.quantity = quantity;
        this.dateCreated = dateCreated;
        this.dateModified = System.currentTimeMillis() / 1000;
    }

    public PortfolioAsset merge(PortfolioAsset other) {
        double thisAggregatePrice = this.price * this.quantity;
        double otherAggregatePrice = other.getPrice() * other.getQuantity();
        this.quantity += other.getQuantity();
        this.price = (thisAggregatePrice + otherAggregatePrice) / this.quantity;
        this.price = Math.round(this.price * 100.0) / 100.0;

        return this;
    }

    public Map<String, String> getDateCreatedStringMap() {
        Map<String, String> dates = new HashMap<>();
        long unixCreated = this.dateCreated;
        Date created = new Date(unixCreated * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'+08:00'");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String createdDate = jdf.format(created);

        dates.put("dateCreated", createdDate);

        return dates;
    }

    public Map<String, String> getDateModifiedStringMap() {
        Map<String, String> dates = new HashMap<>();
        long unixModified = this.dateModified;
        Date modified = new Date(unixModified * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'+08:00'");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String modifiedDate = jdf.format(modified);

        dates.put("dateModified", modifiedDate);

        return dates;
    }

}
