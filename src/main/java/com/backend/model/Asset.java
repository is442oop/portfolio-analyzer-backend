package com.backend.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Data
@Entity
@Table(name = "asset", schema = "prod")
public class Asset {
    @Id
    @Column(name = "asset_ticker")
    private String assetTicker;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "asset_description")
    private String assetDescription;

    @Column(name = "asset_industry")
    private String assetIndustry;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type")
    private AssetType assetType;

    protected Asset() {
    }

}
