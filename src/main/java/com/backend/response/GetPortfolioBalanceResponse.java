package com.backend.response;

import lombok.Data;
import java.util.List;
import java.util.HashMap;

@Data
public class GetPortfolioBalanceResponse {
    private List<HashMap<String, Object>> portfolioHistoryData;
}
