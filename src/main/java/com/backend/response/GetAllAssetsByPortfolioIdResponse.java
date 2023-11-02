package com.backend.response;

import com.backend.model.PortfolioAsset;
import java.util.List;

import lombok.Data;

@Data
public class GetAllAssetsByPortfolioIdResponse {
    private List<PortfolioAsset> portfolioAssetList;
}
