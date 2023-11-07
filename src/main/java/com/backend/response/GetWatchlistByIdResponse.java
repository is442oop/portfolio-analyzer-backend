package com.backend.response;

import java.util.List;
import lombok.Data;

@Data
public class GetWatchlistByIdResponse {
    private String userId;
    private long watchlistId;
    private List<String> watchlist_assets;
}
