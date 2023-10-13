package com.backend.response;

import java.util.List;

import com.backend.model.Portfolio;

import lombok.Data;

@Data
public class FindUserPortfolios {
    List<Portfolio> portfolioList;

}
