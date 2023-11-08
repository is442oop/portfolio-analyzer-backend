package com.backend.model;

import lombok.Data;
// import java.time.LocalDate;
import java.util.List;

// import com.fasterxml.jackson.annotation.JsonBackReference;
// import org.hibernate.annotations.Type;

import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// import com.fasterxml.jackson.annotation.JsonBackReference;

// import jakarta.websocket.ClientEndpoint;
import jakarta.persistence.Column;
import jakarta.persistence.SequenceGenerator;
// import jakarta.persistence.GenerationType;


@Data
@Entity
@Table(name = "watchlist", schema = "prod")
@SequenceGenerator(name = "watchlist-sequence-gen", sequenceName = "watchlist_id_seq" , allocationSize = 1)

public class Watchlist {
    // @Id
    // // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset-sequence-gen")
    // @Column(name = "asset_id")
    // private long assetId;
   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "watchlist-sequence-gen")
   @Column(name = "watchlist_id")
    private long wid;

    // @JsonBackReference
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name="user_id", nullable = false, insertable=false, updatable=false)
    // private User user;

    @Column(name = "user_id")
    private String uid;

    // Assuming asset_ids are stored as a string of comma-separated values
    @Column(name = "watchlist_assets")
    private List<String> assets;

    protected Watchlist() {
    }

    public Watchlist (String user_id, List<String> assets) {
        this.uid = user_id;
        this.assets = assets;
    }

    // public List<String> getAssets() {
    //     return this.assets;
    // }
   
}
