package com.backend.response;

import java.util.List;

import lombok.Data;

@Data
public class UpdateWatchlistAssetsResponse {
    private List<String> watchlist_assets;
}
