package com.backend.response;

import java.util.Map;
import java.util.List;

import lombok.Data;

@Data
public class GetWatchlistPriceDataResponse {
    private  List<Map<String, Object>> watchlistAssetPriceMap;
}
