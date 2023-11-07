package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.backend.model.Watchlist;
// import com.backend.model.Asset;
// import java.util.List;

@RepositoryRestResource(exported = false)
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    Watchlist findByUid(String uid);
    Watchlist findByWid(long wid);
    // Watchlist findByWatchlist_id(long watchlist_id);
}