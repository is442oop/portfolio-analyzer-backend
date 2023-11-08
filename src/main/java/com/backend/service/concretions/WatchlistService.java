package com.backend.service.concretions;

// import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

// import com.backend.exception.UserNotFoundException;
// import com.backend.model.Asset;
import com.backend.model.Watchlist;
import com.backend.repository.WatchlistRepository;

@Service
public class WatchlistService implements com.backend.service.abstractions.IWatchlistService {
    private final WatchlistRepository repository;

    @Autowired
    public WatchlistService(WatchlistRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Watchlist> findAll() {
        return repository.findAll();
    }

    @Override
    public Watchlist findByWid(long wid) {
        Watchlist watchlist = repository.findByWid(wid);
        if (watchlist == null) {
            throw new ResourceNotFoundException("Watchlist not found with id " + wid);
        }
        // List<String> assets = watchlist.getAssets();
        return watchlist;
    }

    @Override
    public Watchlist findByUid(String uid) {
        Watchlist watchlist = repository.findByUid(uid);
        // .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found with id " + id));
        return watchlist;
    }    
    @Override
    public void updateWatchlist(Watchlist watchlist) {
        repository.save(watchlist);
    }

    // @Override
    // public void clearWatchlist(long id) {
    //     Watchlist watchlist = repository.findById(id)
    //      .orElseThrow(() -> new ResourceNotFoundException("Watchlist not found with id " + id));
    //     List<String> assets = watchlist.getAssets();
    //     assets.clear();
    //     repository.save(watchlist);
    // }

    // @Override
    // public void removeWatchlist(Watchlist watchlist) {
    //     repository.delete(watchlist);
    // }

    @Override
    public Watchlist createWatchlist(Watchlist watchlist) {
        return repository.save(watchlist);
    }

}


