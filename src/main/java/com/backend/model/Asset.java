package com.backend.model;

import lombok.Data;
// import java.time.LocalDate;
// import java.util.List;

// import com.fasterxml.jackson.annotation.JsonBackReference;
// import org.hibernate.annotations.Type;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
// import jakarta.websocket.ClientEndpoint;
import jakarta.persistence.Column;
// import jakarta.persistence.SequenceGenerator;
// import jakarta.persistence.GenerationType;


@Data
@Entity
@Table(name = "asset", schema = "prod")
// @SequenceGenerator(name = "asset-sequence-gen", sequenceName = "asset_id_seq" , allocationSize = 1)

public class Asset {
    // @Id
    // // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset-sequence-gen")
    // @Column(name = "asset_id")
    // private long assetId;
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
