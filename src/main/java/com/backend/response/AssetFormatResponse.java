package com.backend.response;

import lombok.Data;
@Data
public class AssetFormatResponse {
    private String value;
    private String label;
    private String ticker;
}


// value: "apple",
// label: "Apple",
// ticker: "AAPL",