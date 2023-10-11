package com.backend.response;

import java.util.List;

import com.backend.model.Portfolio;

import lombok.Data;

@Data
public class FindAllPortfoliosResponse {
    List<Portfolio> portfolioList;
}
