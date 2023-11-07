package com.backend.response;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GetUserOverallPortfolioBalanceResponse {
    private List<Map<String, Object>> overallPortfolioHistoryData;
}
