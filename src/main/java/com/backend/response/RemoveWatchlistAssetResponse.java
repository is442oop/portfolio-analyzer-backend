package com.backend.response;

import java.util.List;

import lombok.Data;

@Data
public class RemoveWatchlistAssetResponse {
    private List<String> watchlist_assets;
}
