package com.backend.model;

import lombok.Data;
import java.time.LocalDate;

// import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;


@Data
@Entity
@Table(name="asset_ref_data", schema = "prod")
@SequenceGenerator(name = "asset-ref-data-sequence-gen", sequenceName = "asset_ref_data_id_seq" , allocationSize = 1)
public class AssetRefData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset-ref-data-sequence-gen")
    @Column(name = "asset_ref_data_id")
    private long assetRefId;

    @Column(name = "asset_id")
    long assetId;

    @Column(name = "day_record")
    LocalDate dayRecord;

    @Column(name = "adjusted_close_decimal")
    float adjustedCloseDecimal;

}
