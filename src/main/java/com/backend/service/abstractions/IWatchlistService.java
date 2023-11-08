package com.backend.service.abstractions;

import java.util.List;
// import com.backend.model.Asset;
import com.backend.model.Watchlist;

public interface IWatchlistService {

    List<Watchlist> findAll();
    Watchlist findByWid(long id);
    Watchlist findByUid(String id);
    void updateWatchlist(Watchlist watchlist);
    // void clearWatchlist(Watchlist watchlist);
    Watchlist createWatchlist(Watchlist watchlist);
}
