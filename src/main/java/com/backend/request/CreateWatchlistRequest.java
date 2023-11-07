package com.backend.request;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateWatchlistRequest {
    private String userId;
    private List<String> watchlist_assets;
}
