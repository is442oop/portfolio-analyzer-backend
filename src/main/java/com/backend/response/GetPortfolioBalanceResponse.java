package com.backend.response;

import lombok.Data;
import java.util.Map;

@Data
public class GetPortfolioBalanceResponse {
    private Map<Long, Double> dailyBalance;
}
