package com.backend.request;

// import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateWatchlistAssetsRequest {
    private String userId;
    private String watchlist_asset;
}
