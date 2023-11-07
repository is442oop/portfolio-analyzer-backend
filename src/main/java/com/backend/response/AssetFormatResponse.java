package com.backend.response;

import lombok.Data;
@Data
public class AssetFormatResponse {
    private String assetValue;
    private String assetName;
    private String assetTicker;
}


// value: "apple",
// label: "Apple",
// ticker: "AAPL",