package com.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import com.backend.exception.PortfolioAssetNotFoundException;
import com.backend.configuration.Constants;
import com.backend.exception.BadRequestException;
import com.backend.exception.WatchlistNotFoundException;
// import com.backend.model.Portfolio;
// import com.backend.model.Portfolio;
// import com.backend.model.Asset;
import com.backend.model.Watchlist;
import com.backend.service.abstractions.IWatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.backend.request.CreateWatchlistRequest;
// import com.backend.request.UpdatePortfolioMetaDataRquest;
import com.backend.response.CreateWatchlistResponse;
import com.backend.response.GetWatchlistByIdResponse;
// import com.backend.response.UpdatePortfolioMetadataResponse;
import com.backend.request.UpdateWatchlistAssetsRequest;
import com.backend.response.UpdateWatchlistAssetsResponse;
import com.backend.request.RemoveWatchlistAssetRequest;
import com.backend.response.RemoveWatchlistAssetResponse;

// import java.util.List;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class WatchlistController {
    Logger logger = LoggerFactory.getLogger(WatchlistController.class);
    private final IWatchlistService watchlistService;

    @Autowired
    public WatchlistController(IWatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping(path = "/watchlist")
    public CreateWatchlistResponse createWatchlist(@RequestBody CreateWatchlistRequest request) {

		if (request.getWatchlist_assets() == null || request.getUserId()==null || request.getUserId().isEmpty()) {
			throw new BadRequestException(Constants.MESSAGE_MISSINGPORTFOLIODESC);
		}

		logger.info("Beginning creation of new watchlist with the following details");
		logger.info("Watchlist User ID: " + request.getUserId());
		logger.info("New Watchlist Assets: " + request.getWatchlist_assets());
		Watchlist watchlist = watchlistService.createWatchlist(
				new Watchlist(
						request.getUserId(),
						request.getWatchlist_assets()
                )
        );

		CreateWatchlistResponse response = new CreateWatchlistResponse();
		response.setWatchlistId(watchlist.getWid());
		response.setUserId(watchlist.getUid());
		response.setWatchlist_assets(watchlist.getAssets());

		return response;
    }

    // @GetMapping
    // public List<Watchlist> getAllWatchlists() {
    //     return watchlistService.getAllWatchlists();
    // }

    @GetMapping(path = "/watchlist/{wid}")
    public GetWatchlistByIdResponse getWatchlist(@PathVariable long wid) {
    Watchlist watchlist = watchlistService.findByWid(wid);

	// if (watchlist == null) {
	// 		throw new WatchlistNotFoundException(wid);
	// 	}
    GetWatchlistByIdResponse response = new GetWatchlistByIdResponse();
    response.setUserId(watchlist.getUid());
    response.setWatchlistId(watchlist.getWid());
    response.setWatchlist_assets(watchlist.getAssets());

    return response;
    }


    @PutMapping(path = "/watchlist/{wid}/add")
	public UpdateWatchlistAssetsResponse addAssetToWatchlist(@PathVariable long wid,
			@RequestBody UpdateWatchlistAssetsRequest body) {
		Watchlist watchlist = watchlistService.findByWid(wid);
		// if (watchlist == null) {
		// 	throw new PortfolioAssetNotFoundException(id);
		// }
		logger.info("Beginning update of watchlist with the following details");
		logger.info("Watchlist ID: " + wid);
		logger.info("New Asset Name: " + body.getWatchlist_asset());

		String asset = body.getWatchlist_asset();
        List<String> assets = watchlist.getAssets();
        assets.add(asset);
	
		if (asset != null && !asset.isEmpty()) {
			watchlist.setAssets(assets);
		}

		watchlistService.updateWatchlist(watchlist);

		logger.info("Watchlist updated successfully");

		UpdateWatchlistAssetsResponse response = new UpdateWatchlistAssetsResponse();
		response.setWatchlist_assets(watchlist.getAssets());

		return response;
	}

    @PutMapping(path = "/watchlist/{wid}/remove")
    public RemoveWatchlistAssetResponse removeAssetFromWatchlist(@PathVariable long wid,
			@RequestBody RemoveWatchlistAssetRequest body) {
                Watchlist watchlist = watchlistService.findByWid(wid);
        //         if (watchlist == null) {
		// 	throw new PortfolioAssetNotFoundException(id);
		// }

        logger.info("Beginning update of watchlist with the following details");
		logger.info("Watchlist ID: " + wid);
		logger.info("New Asset Name: " + body.getWatchlist_asset());

        String asset = body.getWatchlist_asset();
        List<String> assets = watchlist.getAssets();
        assets.remove(asset);

        if (asset != null && !asset.isEmpty()) {
			watchlist.setAssets(assets);
		}

        watchlistService.updateWatchlist(watchlist);

        logger.info("Watchlist updated successfully");

        RemoveWatchlistAssetResponse response = new RemoveWatchlistAssetResponse();
		response.setWatchlist_assets(watchlist.getAssets());

                return response;
            }
}